package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.*;
import com.lkimilhol.paymentSystem.global.CardPaymentInfo;
import com.lkimilhol.paymentSystem.global.error.CustomException;
import com.lkimilhol.paymentSystem.global.error.ErrorCode;
import com.lkimilhol.paymentSystem.responseApi.CardCancelResponse;
import com.lkimilhol.paymentSystem.responseApi.CardGetResponse;
import com.lkimilhol.paymentSystem.responseApi.CardPaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class CardApiService {

    @Autowired
    CardPaymentService cardPaymentService;

    @Autowired
    CardSendDataService cardSendDataService;

    @Autowired
    CardCancelService cardCancelService;

    @Autowired
    CardAdminService cardAdminService;

    @Autowired
    CardDataService cardDataService;

    public CardApiService() {
    }

    public CardGetResponse get(String uniqueId) {
        CardAdmin cardAdmin = getCardAdmin(uniqueId);
        CardBreakdown cardBreakdown = cardDataService.extractPayment(cardAdmin);

        return CardGetResponse.builder()
                .uniqueId(uniqueId)
                .cardNum(cardDataService.getCommonUtility().setMask(cardBreakdown.getCardNumber()))
                .expiryDate(cardBreakdown.getExpiryDate())
                .cvc(cardBreakdown.getCvc())
                .amount(cardBreakdown.getAmount())
                .vat(cardBreakdown.getVat())
                .build();
    }

    //TODO 작업 단위를 더 쪼갤 수 있도록 할 것!
    public CardPaymentResponse pay(CardPayment cardPayment) {
        // 발급 받은 seq로 uniqueId 생성
        CardAdmin cardAdmin = addCardAdmin();

        // 새로운 unique id 생성
        setCardPaymentUniqueId(cardAdmin, cardPayment);

        //카드사에 전송할 data 생성
        String totalData = makeSendData(cardPayment);

        // data 유효성 검사
        checkTotalDataLength(totalData);

        //update cardAdmin
        cardAdmin.setUniqueId(cardPayment.getUniqueId());
        cardAdmin.setCardData(totalData);

        cardPaymentService.save(cardPayment);
        cardSendDataService.save(totalData);

        CardPaymentResponse response = new CardPaymentResponse();
        response.setCardData(totalData);
        response.setUniqueId(cardPayment.getUniqueId());

        return response;
    }

    public CardCancelResponse cancel(CardCancel cardCancel) {
        //실제 정상적으로 발급된 uniqueId 인지 체크
        CardAdmin cardAdmin = getCardAdmin(cardCancel.getUniqueId());

        //카드 정보 추출
        CardBreakdown cardBreakdown = cardDataService.extractPayment(cardAdmin);
        String uniqueId = cardAdmin.getUniqueId();

        // 발급 받은 seq로 uniqueId 생성
        CardAdmin cardCancelAdmin = new CardAdmin();
        cardAdminService.save(cardCancelAdmin);

        long adminSeq = cardCancelAdmin.getSeq();
        String newUniqueId = cardDataService.getCommonUtility().generateUniqueId(adminSeq);
        cardCancel.setUniqueId(newUniqueId);
        cardCancel.setVat(cardDataService.calculateVat(cardCancel.getAmount(), cardCancel.getVat()));
        cardBreakdown.setUniqueId(newUniqueId);

        //카드사에 전송할 data 생성
        String totalData = makeSendData(cardBreakdown);

        //data 유효성 검사
        checkTotalDataLength(totalData);

        // 데이터 업데이트
        cardCancelAdmin.setPaymentStatus(false);
        cardCancelAdmin.setUniqueId(newUniqueId);
        cardCancelAdmin.setCardData(totalData);

        // cardPayment 결제 취소 반영
        CardPayment cardPayment = getCardPayment(uniqueId);

        // 이미 결제를 취소했었다면
        checkCardPaymentCancel(cardPayment);

        // 부분 취소의 경우
        if (cardCancel.isPartCancel()) {
            checkPartCancel(cardPayment, cardCancel);

            int payAmount = cardPayment.getAmount();
            int payVat = cardPayment.getVat();

            int cancelAmount = cardCancel.getAmount();
            int cancelVat = cardCancel.getVat();

            int amount = payAmount - cancelAmount;
            int vat = payVat - cancelVat;

            if (amount == 0 && vat == 0) {
                cardPayment.setPaymentComplete(false);
            }
            cardPayment.setAmount(amount);
            cardPayment.setVat(vat);
        } else {
            cardPayment.setPaymentComplete(false);
            int paymentTotalAmount = cardPayment.getAmount() + cardPayment.getVat();
            cardCancel.setCardPayment(cardPayment);

            int cancelTotalAmount = cardCancel.getAmount() + cardCancel.getVat();

            //취소 금액과 결제 금액(금액 + 부가세)가 다를 경우
            if (cancelTotalAmount != paymentTotalAmount) {
                throw new CustomException(ErrorCode.NOT_EQUAL_TOTAL_AMOUNT);
            }
        }

        cardBreakdown.setUniqueId(uniqueId);
        cardSendDataService.save(totalData);
        cardCancelService.save(cardCancel);

        CardCancelResponse response = new CardCancelResponse();
        response.setCancelData(totalData);
        response.setUniqueId(newUniqueId);
        return response;
    }

    private CardAdmin addCardAdmin() {
        CardAdmin cardAdmin = new CardAdmin();
        cardAdmin.setPaymentStatus(true);
        cardAdminService.save(cardAdmin);
        return cardAdmin;
    }

    private void setCardPaymentUniqueId(CardAdmin cardAdmin, CardPayment cardPayment) {
        String uniqueId = cardDataService.getCommonUtility().generateUniqueId(cardAdmin.getSeq());
        cardPayment.setUniqueId(uniqueId);
        cardPayment.setVat(cardDataService.calculateVat(cardPayment.getAmount(), cardPayment.getVat()));
    }

    private String makeSendData(CardPayment cardPayment) {
        String encryptedCardInfo = cardDataService.getAes256Utility().encryptCardInfo(cardPayment.getCardNumber(), cardPayment.getExpiryDate(), cardPayment.getCvc());
        String data = cardDataService.makeData(cardPayment, "", encryptedCardInfo);
        String header = cardDataService.makeHeader(CardPaymentInfo.CARD_PAYMENT, cardPayment.getUniqueId());

        checkHeaderDataLength(header);

        return header + data;
    }

    private String makeSendData(CardBreakdown cardBreakdown) {
        String encryptedCardInfo = cardDataService.getAes256Utility().encryptCardInfo(cardBreakdown.getCardNumber(), cardBreakdown.getExpiryDate(), cardBreakdown.getCvc());
        String data = cardDataService.makeData(cardBreakdown, "", encryptedCardInfo);
        String header = cardDataService.makeHeader(CardPaymentInfo.CARD_PAYMENT, cardBreakdown.getUniqueId());

        checkHeaderDataLength(header);

        return header + data;
    }

    private void checkHeaderDataLength(String header) {
        if (header.length() != CardPaymentInfo.HEADER_SIZE) {
            throw new CustomException(ErrorCode.INVALID_HEADER_DATA_LEN);
        }
    }

    private void checkTotalDataLength(String totalData) {
        if (totalData.length() != CardPaymentInfo.TOTAL_CARD_DATA_LEN) {
            throw new CustomException(ErrorCode.INVALID_CARD_DATA_LEN);
        }
    }

    private CardAdmin getCardAdmin(String uniqueId) {
        Optional<CardAdmin> cardAdmin = cardAdminService.findByUniqueId(uniqueId);
        cardAdmin.orElseThrow(() -> {
            throw new CustomException(ErrorCode.NOT_FOUND_DATA_BY_UNIQUE_ID);
        });

        return cardAdmin.get();
    }

    private CardPayment getCardPayment(String uniqueId) {
        Optional<CardPayment> optionalCardPayment = cardPaymentService.findByUniqueId(uniqueId);
        optionalCardPayment.orElseThrow(() -> {
            throw new CustomException(ErrorCode.NOT_FOUND_PAYMENT_DATA);
        });

        return optionalCardPayment.get();
    }

    private void checkCardPaymentCancel(CardPayment cardPayment) {
        if (!cardPayment.isPaymentComplete()) {
            throw new CustomException(ErrorCode.ALREADY_CANCEL);
        }
    }

    private void checkPartCancel(CardPayment cardPayment, CardCancel cardCancel) {
        int payAmount = cardPayment.getAmount();
        int payVat = cardPayment.getVat();

        int cancelAmount = cardCancel.getAmount();
        int cancelVat = cardCancel.getVat();

        if (cancelAmount > payAmount) {
            throw new CustomException(ErrorCode.NOT_ENOUGH_PAY_AMOUNT);
        }
        if (cancelVat > payVat) {
            throw new CustomException(ErrorCode.INVALID_PAY_VAT);
        }
        if (cancelAmount == payAmount && cancelVat != payVat) {
            throw new CustomException(ErrorCode.INVALID_PAY_VAT);
        }
    }
}