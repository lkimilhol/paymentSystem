package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.CardBreakdown;
import com.lkimilhol.paymentSystem.domain.CardPayment;
import com.lkimilhol.paymentSystem.global.CardPaymentInfo;
import com.lkimilhol.paymentSystem.global.common.AES256Utility;
import com.lkimilhol.paymentSystem.global.common.CommonUtility;
import lombok.Getter;

@Getter
public class CardDataService {

    CommonUtility commonUtility;
    AES256Utility aes256Utility;

    public CardDataService() {
        commonUtility = new CommonUtility();
        aes256Utility = new AES256Utility();
    }

    protected String makeHeader(String separate, String uniqueId) {
        String dataLenString = commonUtility.appendNumericSpace(CardPaymentInfo.TOTAL_CARD_DATA_LEN - CardPaymentInfo.COMMON_CARD_DATA_LEN, CardPaymentInfo.COMMON_CARD_DATA_LEN);
        String dataSeparate = commonUtility.appendStringSpace(separate, CardPaymentInfo.COMMON_DATA_SEPARATION_LEN);
        String dataUniqueId = commonUtility.appendStringSpace(uniqueId, CardPaymentInfo.COMMON_DATA_UNIQUE_ID_LEN);

        return dataLenString
                + dataSeparate
                + dataUniqueId;
    }


    protected String makeData(CardPayment cardPayment, String originUniqueId, String encryptedCardInfo) {
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

    protected String makeData(CardBreakdown cardBreakdown, String originUniqueId, String encryptedCardInfo) {
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

    protected CardBreakdown extractPayment(String uniqueId, String data) {
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

    protected int calculateVat(int amount, int vat) {
        return vat == -1 ? Math.round((float) amount / 11) : vat;
    }
}
