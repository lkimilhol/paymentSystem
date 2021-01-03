package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.CardAdmin;
import com.lkimilhol.paymentSystem.domain.CardCancel;
import com.lkimilhol.paymentSystem.domain.CardPayment;
import com.lkimilhol.paymentSystem.domain.CardSendData;
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
public class CardService {

    CommonUtility commonUtility;
    AES256Utility aes256Utility;

    @Autowired
    CardPaymentService cardPaymentService;

    @Autowired
    CardSendDataService cardSendDataService;

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

        // 카드사에 전송할 data 생성
        String encryptedCardInfo = aes256Utility.encryptCardInfo(cardPayment);
        String data = makeData(cardPayment, encryptedCardInfo);
        int cardDataLen = data.length();
        String header = makeHeader(cardDataLen, CardPaymentInfo.CARD_PAYMENT, seq);
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

        return extractPayment(uniqueId, cardAdmin.get().getCardData());
    }

    public CardCancelResponse cancel(CardCancel cardCancel) {
        return null;
    }

    private String makeHeader(int dataLen, String separate, long seq) {
        String dataLenString = commonUtility.appendNumericSpace(dataLen, CardPaymentInfo.COMMON_CARD_DATA_LEN);
        String dataSeparate = commonUtility.appendStringSpace(separate, CardPaymentInfo.COMMON_DATA_SEPARATION_LEN);
        String dataUniqueId = commonUtility.appendStringSpace(Long.toString(seq), CardPaymentInfo.COMMON_DATA_UNIQUE_ID_LEN);

        return dataLenString
                + dataSeparate
                + dataUniqueId;
    }


    private String makeData(CardPayment cardPayment, String encryptedCardInfo) {
        String cardNumber = commonUtility.appendNumericNumberLeft(cardPayment.getCardNumber(),
                CardPaymentInfo.CARD_NUMBER_LEN);
        String installment = commonUtility.appendNumericZero(cardPayment.getInstallment(),
                CardPaymentInfo.CARD_INSTALLMENT_LEN);
        String expiryDate = commonUtility.zeroFill(cardPayment.getExpiryDate(),
                CardPaymentInfo.CARD_EXPIRY_DATE_LEN);
        String cvc = commonUtility.zeroFill(cardPayment.getCvc(),
                CardPaymentInfo.CARD_CVC_LEN);
        String amount = commonUtility.appendNumericSpace(cardPayment.getAmount(),
                CardPaymentInfo.CARD_AMOUNT_LEN);
        String vat = commonUtility.appendNumericZero(cardPayment.getVat(),
                CardPaymentInfo.CARD_VAT_LEN);
        String originalPayment = commonUtility.appendStringSpace("",
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

    private CardGetResponse extractPayment(String uniqueId, String data) {
        int start = CardPaymentInfo.CARD_DATA_START_IDX;
        int end = start + CardPaymentInfo.CARD_AMOUNT_LEN;
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

        CardGetResponse cardGetResponse = new CardGetResponse();
        cardGetResponse.setUniqueId(uniqueId);
        cardGetResponse.setCardNum(commonUtility.setMask(cardNumber));
        cardGetResponse.setExpiryDate(expiryDate);
        cardGetResponse.setCvc(cvc);
        cardGetResponse.setAmount(Integer.parseInt(amount));
        cardGetResponse.setVat(Integer.parseInt(vat));

        return cardGetResponse;
    }
}