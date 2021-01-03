package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.CardPayment;
import com.lkimilhol.paymentSystem.domain.CardSendData;
import com.lkimilhol.paymentSystem.global.CardPaymentInfo;
import com.lkimilhol.paymentSystem.global.common.AES256Utility;
import com.lkimilhol.paymentSystem.global.common.CommonUtility;
import com.lkimilhol.paymentSystem.global.error.CustomException;
import com.lkimilhol.paymentSystem.global.error.ErrorCode;
import com.lkimilhol.paymentSystem.repository.CardPaymentRepository;
import com.lkimilhol.paymentSystem.repository.CardSendDataRepository;
import com.lkimilhol.paymentSystem.responseApi.CardPaymentResponse;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class CardService {

    private CardPaymentRepository cardPaymentRepository;
    private CardSendDataRepository cardSendDataRepository;

    CommonUtility commonUtility;
    AES256Utility aes256Utility;

    public CardService(CardPaymentRepository cp, CardSendDataRepository cs) {
        this.cardPaymentRepository = cp;
        this.cardSendDataRepository = cs;
        commonUtility = new CommonUtility();
        aes256Utility = new AES256Utility();
    }


    public CardPaymentResponse pay(CardPayment cardPayment) {
        cardPayment.setInsertTime(LocalDateTime.now());
        cardPaymentRepository.save(cardPayment);
        long cardPaymentId = cardPayment.getUniqueId();

        String encryptedCardInfo = aes256Utility.encryptCardInfo(cardPayment);

        int vatAmount = calculateVat(cardPayment.getAmount(), cardPayment.getVat());
        cardPayment.setVat(vatAmount);

        String cardData = makeCardData(cardPayment, encryptedCardInfo);
        int cardDataLen = cardData.length();
        String commonData = makeCommonData(cardDataLen, CardPaymentInfo.CARD_PAYMENT, cardPaymentId);
        String totalData = commonData + cardData;

        if (totalData.length() != CardPaymentInfo.TOTAL_CARD_DATA_LEN) {
            throw new CustomException(ErrorCode.INVALID_CARD_DATA_LEN);
        }

        String uniqueId = commonUtility.generateUniqueId(cardPaymentId);

        CardSendData cardSendData = new CardSendData();

        cardSendData.setUniqueId(uniqueId);
        cardSendData.setCardData(totalData);
        cardSendData.setSendTime(LocalDateTime.now());

        cardSendDataRepository.save(cardSendData);

        CardPaymentResponse response = new CardPaymentResponse();
        response.setCardData(cardSendData.getCardData());
        response.setUniqueId(cardSendData.getUniqueId());

        return response;
    }

    private int calculateVat(int amount, int vat) {
        amount += vat == 0 ? Math.round((float) amount / 11) : vat;
        return amount;
    }

    private String makeCommonData(int dataLen, String separate, long uniqueId) {
        String dataLenString = commonUtility.appendNumericSpace(dataLen, CardPaymentInfo.COMMON_CARD_DATA_LEN);
        String dataSeparate = commonUtility.appendStringSpace(separate, CardPaymentInfo.COMMON_DATA_SEPARATION_LEN);
        String dataUniqueId = commonUtility.appendStringSpace(Long.toString(uniqueId), CardPaymentInfo.COMMON_DATA_UNIQUE_ID_LEN);

        return dataLenString
                + dataSeparate
                + dataUniqueId;
    }


    private String makeCardData(CardPayment cardPayment, String encryptedCardInfo) {
        String cardNumber = commonUtility.appendNumericNumberLeft(cardPayment.getCardNumber(),
                CardPaymentInfo.CARD_NUMBER_LEN);
        String installment = commonUtility.appendNumericZero(cardPayment.getInstallment(),
                CardPaymentInfo.CARD_INSTALLMENT_LEN);
        String expiryDate = commonUtility.appendNumericNumberLeft(cardPayment.getExpiryDate(),
                CardPaymentInfo.CARD_EXPIRY_DATE_LEN);
        String csv = commonUtility.appendNumericNumberLeft(cardPayment.getCsv(),
                CardPaymentInfo.CARD_CSV_LEN);
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
                csv +
                amount +
                vat +
                originalPayment +
                encryptedCardInfo +
                spareField;
    }



//    public Optional<CardPayment> findById(long id) {
//        return cardRepository.findById(id);
//    }

//    public List<CardPayment> findCardList() {
//        return cardRepository.findAll();
//    }
}
