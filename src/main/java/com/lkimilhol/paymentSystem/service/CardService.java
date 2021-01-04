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
import org.hibernate.dialect.identity.JDataStoreIdentityColumnSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class CardService {

    CommonUtility commonUtility;
    AES256Utility aes256Utility;

    @Autowired
    CardPaymentService cardPaymentService;

    @Autowired
    CardSendDataService cardSendDataService;

    @Autowired
    CardCancelService cardCancelService;

    @Autowired
    CardAdminService cardAdminService;

    public CardService() {
        commonUtility = new CommonUtility();
        aes256Utility = new AES256Utility();
    }

    public CardPaymentResponse pay(CardPayment cardPayment) {
        // seq 발급 받음
        CardAdmin cardAdmin = new CardAdmin();
        cardAdmin.setPaymentStatus(true);
        cardAdminService.save(cardAdmin);

        // 발급 받은 seq로 uniqueId 생성
        long seq = cardAdmin.getSeq();
        String uniqueId = commonUtility.generateUniqueId(seq);

        cardPayment.setVat(calculateVat(cardPayment.getAmount(), cardPayment.getVat()));

        // 카드사에 전송할 data 생성
        String encryptedCardInfo = aes256Utility.encryptCardInfo(cardPayment.getCardNumber(), cardPayment.getExpiryDate(), cardPayment.getCvc());
        String data = makeData(cardPayment, "", encryptedCardInfo);
        int cardDataLen = data.length();
        String header = makeHeader(cardDataLen, CardPaymentInfo.CARD_PAYMENT, uniqueId);
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
        cardPayment.setUniqueId(uniqueId);
        cardPaymentService.save(cardPayment);
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
        CardBreakdown cardBreakdown = extractPayment(uniqueId, cardAdmin.get().getCardData());
        CardGetResponse cardGetResponse = new CardGetResponse();
        cardGetResponse.setUniqueId(uniqueId);
        cardGetResponse.setCardNum(commonUtility.setMask(cardBreakdown.getCardNumber()));
        cardGetResponse.setExpiryDate(cardBreakdown.getExpiryDate());
        cardGetResponse.setCvc(cardBreakdown.getCvc());
        cardGetResponse.setAmount(cardBreakdown.getAmount());
        cardGetResponse.setVat(cardBreakdown.getVat());
        return cardGetResponse;
    }

    public CardCancelResponse cancel(CardCancel cardCancel) {
        //실제 정상적으로 발급된 uniqueId 인지 체크
        Optional<CardAdmin> cardAdmin = cardAdminService.findByUniqueId(cardCancel.getUniqueId());
        cardAdmin.orElseThrow(() -> {
            throw new CustomException(ErrorCode.NOT_FOUND_DATA_BY_UNIQUE_ID);
        });

        String uniqueId = cardAdmin.get().getUniqueId();

        CardBreakdown cardBreakdown = extractPayment(uniqueId, cardAdmin.get().getCardData());

        // 발급 받은 seq로 uniqueId 생성
        CardAdmin cardCancelAdmin = new CardAdmin();
        cardAdminService.save(cardCancelAdmin);

        long adminSeq = cardCancelAdmin.getSeq();
        String newUniqueId = commonUtility.generateUniqueId(adminSeq);
        cardCancel.setUniqueId(newUniqueId);
        cardBreakdown.setUniqueId(newUniqueId);

        // 카드사에 전송할 data 생성
        String encryptedCardInfo = aes256Utility.encryptCardInfo(cardBreakdown.getCardNumber(), cardBreakdown.getExpiryDate(), cardBreakdown.getCvc());
        String data = makeData(cardBreakdown, uniqueId, encryptedCardInfo);
        int cardDataLen = data.length();
        String header = makeHeader(cardDataLen, CardPaymentInfo.CARD_CANCEL, newUniqueId);
        String totalData = header + data;

        // data 유효성 검사
        if (header.length() != CardPaymentInfo.HEADER_SIZE) {
            throw new CustomException(ErrorCode.INVALID_HEADER_DATA_LEN);
        }
        if (totalData.length() != CardPaymentInfo.TOTAL_CARD_DATA_LEN) {
            throw new CustomException(ErrorCode.INVALID_CARD_DATA_LEN);
        }

        // 데이터 업데이
        cardCancelAdmin.setPaymentStatus(false);
        cardCancelAdmin.setUniqueId(newUniqueId);
        cardCancelAdmin.setCardData(totalData);

        // cardPayment의 결제 취소 반영
        long paymentSeq = commonUtility.extractSeq(uniqueId);
        Optional<CardPayment> cardPayment = cardPaymentService.findByUniqueId(uniqueId);
        cardPayment.orElseThrow(() -> {
            throw new CustomException(ErrorCode.NOT_FOUND_PAYMENT_DATA);
        });

        // 이미 결제를 취소했었다면
        if (!cardPayment.get().isPaymentStatus()) {
            throw new CustomException(ErrorCode.ALREADY_CANCEL);
        }
        cardPayment.get().setPaymentStatus(false);
        int totalAmount = cardPayment.get().getAmount() + cardPayment.get().getVat();
        cardCancel.setCardPayment(cardPayment.get());

        if (cardCancel.getAmount() != totalAmount) {
            throw new CustomException(ErrorCode.NOT_EQUAL_TOTAL_AMOUNT);
        }

//        cardAdmin.setCardData(totalData);
        cardBreakdown.setUniqueId(uniqueId);
        cardSendDataService.save(totalData);
        cardCancelService.save(cardCancel);

        CardCancelResponse response = new CardCancelResponse();
        response.setCancelData(totalData);
        response.setUniqueId(newUniqueId);
        return response;
    }

    private String makeHeader(int dataLen, String separate, String uniqueId) {
        String dataLenString = commonUtility.appendNumericSpace(dataLen, CardPaymentInfo.COMMON_CARD_DATA_LEN);
        String dataSeparate = commonUtility.appendStringSpace(separate, CardPaymentInfo.COMMON_DATA_SEPARATION_LEN);
        String dataUniqueId = commonUtility.appendStringSpace(uniqueId, CardPaymentInfo.COMMON_DATA_UNIQUE_ID_LEN);

        return dataLenString
                + dataSeparate
                + dataUniqueId;
    }


    private String makeData(CardPayment cardPayment, String originUniqueId, String encryptedCardInfo) {
        String cardNumber = commonUtility.appendStringSpace(cardPayment.getCardNumber(),
                CardPaymentInfo.CARD_NUMBER_LEN);
        String installment = commonUtility.appendNumericZero(cardPayment.getInstallment(),
                CardPaymentInfo.CARD_INSTALLMENT_LEN);
        String expiryDate = commonUtility.appendStringSpace(cardPayment.getExpiryDate(),
                CardPaymentInfo.CARD_EXPIRY_DATE_LEN);
        String cvc = commonUtility.appendStringSpace(cardPayment.getCvc(),
                CardPaymentInfo.CARD_CVC_LEN);
        String amount = commonUtility.appendNumericSpace(cardPayment.getAmount(),
                CardPaymentInfo.CARD_AMOUNT_LEN);
        String vat = commonUtility.appendNumericZero(cardPayment.getVat(),
                CardPaymentInfo.CARD_VAT_LEN);
        String originalPayment = commonUtility.appendStringSpace(originUniqueId,
                CardPaymentInfo.CARD_PAYMENT_ORIGINAL);
        encryptedCardInfo = commonUtility.appendStringSpace(encryptedCardInfo,
                CardPaymentInfo.CARD_ENCRYPTED_DATA_LEN);
        String spareField = commonUtility.appendStringSpace("",
                CardPaymentInfo.SPARE_FIELD);

        return cardNumber +
                installment +
                expiryDate +
                cvc +
                amount +
                vat +
                originalPayment +
                encryptedCardInfo +
                spareField;
    }

    private String makeData(CardBreakdown cardBreakdown, String originUniqueId, String encryptedCardInfo) {
        String cardNumber = commonUtility.appendStringSpace(cardBreakdown.getCardNumber(),
                CardPaymentInfo.CARD_NUMBER_LEN);
        String installment = commonUtility.appendNumericZero(cardBreakdown.getInstallment(),
                CardPaymentInfo.CARD_INSTALLMENT_LEN);
        String expiryDate = commonUtility.appendStringSpace(cardBreakdown.getExpiryDate(),
                CardPaymentInfo.CARD_EXPIRY_DATE_LEN);
        String cvc = commonUtility.appendStringSpace(cardBreakdown.getCvc(),
                CardPaymentInfo.CARD_CVC_LEN);
        String amount = commonUtility.appendNumericSpace(cardBreakdown.getAmount(),
                CardPaymentInfo.CARD_AMOUNT_LEN);
        String vat = commonUtility.appendNumericZero(cardBreakdown.getVat(),
                CardPaymentInfo.CARD_VAT_LEN);
        String originalPayment = commonUtility.appendStringSpace(originUniqueId,
                CardPaymentInfo.CARD_PAYMENT_ORIGINAL);
        encryptedCardInfo = commonUtility.appendStringSpace(encryptedCardInfo,
                CardPaymentInfo.CARD_ENCRYPTED_DATA_LEN);
        String spareField = commonUtility.appendStringSpace("",
                CardPaymentInfo.SPARE_FIELD);

        return cardNumber +
                installment +
                expiryDate +
                cvc +
                amount +
                vat +
                originalPayment +
                encryptedCardInfo +
                spareField;
    }

    private CardBreakdown extractPayment(String uniqueId, String data) {
        int start = CardPaymentInfo.HEADER_SIZE + CardPaymentInfo.CARD_NUMBER_LEN;
        int end = CardPaymentInfo.HEADER_SIZE + CardPaymentInfo.CARD_NUMBER_LEN + CardPaymentInfo.CARD_INSTALLMENT_LEN;
        String installment = data.substring(start, end);

        start = CardPaymentInfo.CARD_DATA_START_IDX;
        end = start + CardPaymentInfo.CARD_AMOUNT_LEN;
        String amount = data.substring(start, end).replaceAll(" ", "");

        start = end;
        end = start + CardPaymentInfo.CARD_VAT_LEN;
        String vat = data.substring(start, end).replaceAll(" ", "");

        start = end + CardPaymentInfo.CARD_PAYMENT_ORIGINAL;
        end = start + CardPaymentInfo.CARD_ENCRYPTED_DATA_LEN;
        String encrypt = data.substring(start, end).replaceAll(" ", "");

        String[] result = aes256Utility.decryptCardInfo(encrypt).split(CardPaymentInfo.CARD_INFORMATION_DELIMITER);

        String cardNumber = result[0];
        String expiryDate = result[1];
        String cvc = result[2];

        CardBreakdown cardBreakdown = new CardBreakdown();
        cardBreakdown.setUniqueId(uniqueId);
        cardBreakdown.setCardNumber(cardNumber);
        cardBreakdown.setInstallment(Integer.parseInt(installment));
        cardBreakdown.setCvc(cvc);
        cardBreakdown.setExpiryDate(expiryDate);
        cardBreakdown.setAmount(Integer.parseInt(amount));
        cardBreakdown.setVat(Integer.parseInt(vat));

        return cardBreakdown;
    }

    private int calculateVat(int amount, int vat) {
        return vat == 0 ? Math.round((float) amount / 11) : vat;
    }
}