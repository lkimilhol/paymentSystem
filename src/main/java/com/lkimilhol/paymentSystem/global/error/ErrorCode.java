package com.lkimilhol.paymentSystem.global.error;

public enum ErrorCode {
    // Common
    NOT_EXISTS_REQUIRED_KEY(400, "-99", "Required key not found in request"),
    INVALID_VALUE(400, "-98", "Request's Value is Invalid"),

    // Payment
    INVALID_HEADER_DATA_LEN(400, "P001", "common header length is invalid"),
    INVALID_CARD_DATA_LEN(400, "P002", "Card data length is invalid"),
    NOT_FOUND_PAYMENT_DATA(400, "P003", "not found payment data"),
    ALREADY_CANCEL(400, "P004", "already cancel"),
    NOT_EQUAL_TOTAL_AMOUNT(400, "P005", "not equal amount"),

    // SendData
    NOT_FOUND_UNIQUE_ID(400, "C001", "not found unique id"),

    // Admin
    NOT_FOUND_DATA_BY_UNIQUE_ID(400, "A001", "not found card admin data"),
    ;

    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}
