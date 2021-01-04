package com.lkimilhol.paymentSystem.global.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // Common
    NOT_EXISTS_REQUIRED_KEY(HttpStatus.BAD_REQUEST, "-99", "Required key not found in request"),
    INVALID_VALUE(HttpStatus.BAD_REQUEST, "-98", "Request's Value is Invalid"),

    // Payment
    INVALID_HEADER_DATA_LEN(HttpStatus.INTERNAL_SERVER_ERROR, "P001", "common header length is invalid"),
    INVALID_CARD_DATA_LEN(HttpStatus.INTERNAL_SERVER_ERROR, "P002", "Card data length is invalid"),
    NOT_FOUND_PAYMENT_DATA(HttpStatus.INTERNAL_SERVER_ERROR, "P003", "not found payment data"),

    // Cancel,
    ALREADY_CANCEL(HttpStatus.BAD_REQUEST, "P004", "already cancel"),
    NOT_EQUAL_TOTAL_AMOUNT(HttpStatus.BAD_REQUEST, "P005", "not equal amount"),
    NOT_ENOUGH_PAY_AMOUNT(HttpStatus.BAD_REQUEST, "P005", "not enough vat"),
    INVALID_PAY_VAT(HttpStatus.BAD_REQUEST, "P005", "not enough vat"),

    // SendData
    NOT_FOUND_UNIQUE_ID(HttpStatus.BAD_REQUEST, "C001", "not found unique id"),

    // Admin
    NOT_FOUND_DATA_BY_UNIQUE_ID(HttpStatus.BAD_REQUEST, "A001", "not found card admin data"),

    // common
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "-1", "not found error"),
    ;


    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(final HttpStatus status, final String code, final String message) {
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

    public HttpStatus getStatus() {
        return status;
    }
}
