package com.lkimilhol.paymentSystem.dto;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lkimilhol.paymentSystem.domain.CardPayment;
import com.lkimilhol.paymentSystem.global.error.CustomException;
import com.lkimilhol.paymentSystem.global.error.ErrorCode;
import lombok.*;

import java.util.regex.Pattern;


@Data

public class CardPaymentDto implements DataTransferObjectService {
    private String[] requireKey = {"cardNumber", "expiryDate", "csv", "installment", "amount"};

    public CardPaymentDto() {}

    @Override
    //todo 키 값 체크도 해줘야 함
    public void checkKey(JsonObject obj) {
        for (String key : this.requireKey) {
            if (!obj.has(key)) {
                throw new CustomException(ErrorCode.NOT_EXISTS_REQUIRED_KEY);
            }
        }
    }

    public CardPayment transferCard(String s) {
        Gson gson = new Gson();
        JsonParser p = new JsonParser();
        JsonObject obj = (JsonObject) p.parse(s);
        checkKey(obj);
        checkValue(obj);

        return gson.fromJson(s, CardPayment.class);
    }

    private void checkValue(JsonObject obj) {
        checkStringValue(obj.get("cardNumber").getAsString(), "^[0-9]{10,16}");
        checkStringValue(obj.get("expiryDate").getAsString(), "^[0-9]{4,4}");
        checkStringValue(obj.get("csv").getAsString(), "^[0-9]{3,3}");
        checkIntegerValue(obj.get("installment").getAsInt(), 0, 12);
        checkIntegerValue(obj.get("amount").getAsInt(), 100, 1000000000);
        if (obj.has("vat")) {
            checkIntegerValue(obj.get("vat").getAsInt(), 0, obj.get("amount").getAsInt());
        }
    }

    private void checkStringValue(String s, String reg) {
        if(!Pattern.matches(reg, s)) {
            throw new CustomException(ErrorCode.INVALID_VALUE);}
    }

    private void checkIntegerValue(int i, int min, int max) {
        if(i < min || i > max) {
            throw new CustomException(ErrorCode.INVALID_VALUE);
        }
    }
}