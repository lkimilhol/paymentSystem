package com.lkimilhol.paymentSystem.global;

public class CardPaymentInfo {
    public static final int TOTAL_CARD_DATA_LEN = 450;

    public static final int CARD_NUMBER_LEN = 20;
    public static final int CARD_INSTALLMENT_LEN = 2;
    public static final int CARD_EXPIRY_DATE_LEN = 4;
    public static final int CARD_CSV_LEN = 3;
    public static final int CARD_AMOUNT_LEN = 10;
    public static final int CARD_VAT_LEN = 10;
    public static final int CARD_PAYMENT_ORIGINAL = 20;
    public static final int CARD_ENCRYPTED_DATA_LEN = 300;
    public static final int SPARE_FIELD = 47;
    public static final String CARD_INFORMATION_DELIMITER = "/";

    public static final String CARD_PAYMENT = "PAYMENT";
    public static final String CARD_CANCEL = "CANCEL";

    public static final int COMMON_CARD_DATA_LEN = 4;
    public static final int COMMON_DATA_SEPARATION_LEN = 10;
    public static final int COMMON_DATA_UNIQUE_ID_LEN = 20;

}
