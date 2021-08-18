package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.CardAdmin;
import com.lkimilhol.paymentSystem.domain.CardBreakdown;
import com.lkimilhol.paymentSystem.domain.CardPayment;
import com.lkimilhol.paymentSystem.global.CardPaymentInfo;
import com.lkimilhol.paymentSystem.global.common.AES256Utility;
import com.lkimilhol.paymentSystem.global.common.CommonUtility;
import lombok.Getter;

@Getter
public class CardDataService {

    private CommonUtility commonUtility;
    private AES256Utility aes256Utility;

    public CardDataService() {
        commonUtility = new CommonUtility();
        aes256Utility = new AES256Utility();
    }

    protected String makeHeader(String separate, String uniqueId) {
        String dataLenString = commonUtility.fillSpace(CardPaymentInfo.TOTAL_CARD_DATA_LEN - CardPaymentInfo.COMMON_CARD_DATA_LEN, CardPaymentInfo.COMMON_CARD_DATA_LEN);
        String dataSeparate = CommonUtility.fillSpace(separate, CardPaymentInfo.COMMON_DATA_SEPARATION_LEN);
        String dataUniqueId = CommonUtility.fillSpace(uniqueId, CardPaymentInfo.COMMON_DATA_UNIQUE_ID_LEN);

        return dataLenString
                + dataSeparate
                + dataUniqueId;
    }

    protected String makeData(CardPayment cardPayment, String originUniqueId, String encryptedCardInfo) {
        String cardNumber = CommonUtility.fillSpace(cardPayment.getCardNumber(),
                CardPaymentInfo.CARD_NUMBER_LEN);
        String installment = CommonUtility.appendNumericZero(cardPayment.getInstallment(),
                CardPaymentInfo.CARD_INSTALLMENT_LEN);
        String expiryDate = CommonUtility.fillSpace(cardPayment.getExpiryDate(),
                CardPaymentInfo.CARD_EXPIRY_DATE_LEN);
        String cvc = CommonUtility.fillSpace(cardPayment.getCvc(),
                CardPaymentInfo.CARD_CVC_LEN);
        String amount = CommonUtility.fillSpace(cardPayment.getAmount(),
                CardPaymentInfo.CARD_AMOUNT_LEN);
        String vat = CommonUtility.appendNumericZero(cardPayment.getVat(),
                CardPaymentInfo.CARD_VAT_LEN);
        String originalPayment = CommonUtility.fillSpace(originUniqueId,
                CardPaymentInfo.CARD_PAYMENT_ORIGINAL);
        encryptedCardInfo = CommonUtility.fillSpace(encryptedCardInfo,
                CardPaymentInfo.CARD_ENCRYPTED_DATA_LEN);
        String spareField = CommonUtility.fillSpace("",
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
        String cardNumber = CommonUtility.fillSpace(cardBreakdown.getCardNumber(),
                CardPaymentInfo.CARD_NUMBER_LEN);
        String installment = CommonUtility.appendNumericZero(cardBreakdown.getInstallment(),
                CardPaymentInfo.CARD_INSTALLMENT_LEN);
        String expiryDate = CommonUtility.fillSpace(cardBreakdown.getExpiryDate(),
                CardPaymentInfo.CARD_EXPIRY_DATE_LEN);
        String cvc = CommonUtility.fillSpace(cardBreakdown.getCvc(),
                CardPaymentInfo.CARD_CVC_LEN);
        String amount = CommonUtility.fillSpace(cardBreakdown.getAmount(),
                CardPaymentInfo.CARD_AMOUNT_LEN);
        String vat = CommonUtility.appendNumericZero(cardBreakdown.getVat(),
                CardPaymentInfo.CARD_VAT_LEN);
        String originalPayment = CommonUtility.fillSpace(originUniqueId,
                CardPaymentInfo.CARD_PAYMENT_ORIGINAL);
        encryptedCardInfo = CommonUtility.fillSpace(encryptedCardInfo,
                CardPaymentInfo.CARD_ENCRYPTED_DATA_LEN);
        String spareField = CommonUtility.fillSpace("",
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

    //TODO 보기 편하게 수정할것
    protected CardBreakdown extractPayment(CardAdmin cardAdmin) {
        String uniqueId = cardAdmin.getUniqueId();
        String data = cardAdmin.getCardData();

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

        return CardBreakdown.builder()
                .uniqueId(uniqueId)
                .cardNumber(cardNumber)
                .installment(Integer.parseInt(installment))
                .cvc(cvc)
                .expiryDate(expiryDate)
                .amount(Integer.parseInt(amount))
                .vat(Integer.parseInt(vat))
                .build();
    }

    protected int calculateVat(int amount, int vat) {
        return vat == -1 ? Math.round((float) amount / 11) : vat;
    }
}
