package com.lkimilhol.paymentSystem.global.error;

public enum ErrorCode {
    // Common
    NOT_EXISTS_REQUIRED_KEY(400, "-99", "Required key not found in request"),
    INVALID_VALUE(400, "-98", "Request's Value is Invalid"),

    // Card
    INVALID_CARD_DATA_LEN(400, "201", "Card data length is invalid"),

    // Coupon
    COUPON_ALREADY_USE(400, "CO001", "Coupon was already used"),
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
