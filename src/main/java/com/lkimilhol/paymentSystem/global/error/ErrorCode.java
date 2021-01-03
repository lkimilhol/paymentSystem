package com.lkimilhol.paymentSystem.global.error;

public enum ErrorCode {
    // Common
    NOT_EXISTS_REQUIRED_KEY(400, "C001", "Required key not found in request"),

    // Member
    EMAIL_DUPLICATION(400, "M001", "Email is Duplication"),

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
