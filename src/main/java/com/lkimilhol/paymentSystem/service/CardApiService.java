package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.*;
import com.lkimilhol.paymentSystem.global.CardPaymentInfo;
import com.lkimilhol.paymentSystem.global.common.AES256Utility;
import com.lkimilhol.paymentSystem.global.common.CommonUtility;
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

    //TODO 작업 단위를 더 쪼갤 수 있도록 할 것!
    public CardPaymentResponse pay(CardPayment cardPayment) {
        // seq 발급 받음
        CardAdmin cardAdmin = new CardAdmin();
        cardAdmin.setPaymentStatus(true);
        cardAdminService.save(cardAdmin);

        // 발급 받은 seq로 uniqueId 생성
        long seq = cardAdmin.getSeq();
        String uniqueId = cardDataService.getCommonUtility().generateUniqueId(seq);
        cardPayment.setUniqueId(uniqueId);
        cardPayment.setVat(cardDataService.calculateVat(cardPayment.getAmount(), cardPayment.getVat()));

        //TODO 카드사에 전송할 data 생성
        String encryptedCardInfo = cardDataService.getAes256Utility().encryptCardInfo(cardPayment.getCardNumber(), cardPayment.getExpiryDate(), cardPayment.getCvc());
        String data = cardDataService.makeData(cardPayment, "", encryptedCardInfo);
        String header = cardDataService.makeHeader(CardPaymentInfo.CARD_PAYMENT, uniqueId);
        String totalData = header + data;

        // data 유효성 검사
        if (header.length() != CardPaymentInfo.HEADER_SIZE) {
            throw new CustomException(ErrorCode.INVALID_HEADER_DATA_LEN);
        }
        if (totalData.length() != CardPaymentInfo.TOTAL_CARD_DATA_LEN) {
            throw new CustomException(ErrorCode.INVALID_CARD_DATA_LEN);
        }

        cardAdmin.setUniqueId(uniqueId);
        cardAdmin.setCardData(totalData);
        cardPaymentService.save(cardPayment);

        cardPayment.setUniqueId(uniqueId);

        cardSendDataService.save(totalData);

        CardPaymentResponse response = new CardPaymentResponse();
        response.setCardData(totalData);
        response.setUniqueId(uniqueId);

        return response;
    }

    public CardGetResponse get(String uniqueId) {
        Optional<CardAdmin> cardAdmin = cardAdminService.findByUniqueId(uniqueId);
        cardAdmin.orElseThrow(() -> {
            throw new CustomException(ErrorCode.NOT_FOUND_UNIQUE_ID);
        });
        CardBreakdown cardBreakdown = cardDataService.extractPayment(uniqueId, cardAdmin.get().getCardData());

        return CardGetResponse.builder()
                .uniqueId(uniqueId)
                .cardNum(cardDataService.getCommonUtility().setMask(cardBreakdown.getCardNumber()))
                .expiryDate(cardBreakdown.getExpiryDate())
                .cvc(cardBreakdown.getCvc())
                .amount(cardBreakdown.getAmount())
                .vat(cardBreakdown.getVat())
                .build();
    }

    public CardCancelResponse cancel(CardCancel cardCancel) {
        //실제 정상적으로 발급된 uniqueId 인지 체크
        Optional<CardAdmin> cardAdmin = cardAdminService.findByUniqueId(cardCancel.getUniqueId());
        cardAdmin.orElseThrow(() -> {
            throw new CustomException(ErrorCode.NOT_FOUND_DATA_BY_UNIQUE_ID);
        });

        String uniqueId = cardAdmin.get().getUniqueId();
        CardBreakdown cardBreakdown = cardDataService.extractPayment(uniqueId, cardAdmin.get().getCardData());

        // 발급 받은 seq로 uniqueId 생성
        CardAdmin cardCancelAdmin = new CardAdmin();
        cardAdminService.save(cardCancelAdmin);

        long adminSeq = cardCancelAdmin.getSeq();
        String newUniqueId = cardDataService.getCommonUtility().generateUniqueId(adminSeq);
        cardCancel.setUniqueId(newUniqueId);
        cardCancel.setVat(cardDataService.calculateVat(cardCancel.getAmount(), cardCancel.getVat()));
        cardBreakdown.setUniqueId(newUniqueId);

        //TODO 카드사에 전송할 data 생성
        String encryptedCardInfo = cardDataService.getAes256Utility().encryptCardInfo(cardBreakdown.getCardNumber(), cardBreakdown.getExpiryDate(), cardBreakdown.getCvc());
        String data = cardDataService.makeData(cardBreakdown, uniqueId, encryptedCardInfo);
        String header = cardDataService.makeHeader(CardPaymentInfo.CARD_CANCEL, newUniqueId);
        String totalData = header + data;

        //TODO data 유효성 검사
        if (header.length() != CardPaymentInfo.HEADER_SIZE) {
            throw new CustomException(ErrorCode.INVALID_HEADER_DATA_LEN);
        }
        if (totalData.length() != CardPaymentInfo.TOTAL_CARD_DATA_LEN) {
            throw new CustomException(ErrorCode.INVALID_CARD_DATA_LEN);
        }

        // 데이터 업데이트
        cardCancelAdmin.setPaymentStatus(false);
        cardCancelAdmin.setUniqueId(newUniqueId);
        cardCancelAdmin.setCardData(totalData);

        // cardPayment 결제 취소 반영
        Optional<CardPayment> optionalCardPayment = cardPaymentService.findByUniqueId(uniqueId);
        optionalCardPayment.orElseThrow(() -> {
            throw new CustomException(ErrorCode.NOT_FOUND_PAYMENT_DATA);
        });

        // 이미 결제를 취소했었다면
        if (!optionalCardPayment.get().isPaymentStatus()) {
            throw new CustomException(ErrorCode.ALREADY_CANCEL);
        }

        CardPayment cardPayment = optionalCardPayment.get();
        // 부분 취소의 경우
        if (cardCancel.isPartCancel()) {
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

            int amount = payAmount - cancelAmount;
            int vat = payVat - cancelVat;

            if (amount == 0 && vat == 0) {
                cardPayment.setPaymentStatus(false);
            }
            cardPayment.setAmount(amount);
            cardPayment.setVat(vat);
        } else {
            cardPayment.setPaymentStatus(false);
            int paymentTotalAmount = cardPayment.getAmount() + cardPayment.getVat();
            cardCancel.setCardPayment(optionalCardPayment.get());

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
}