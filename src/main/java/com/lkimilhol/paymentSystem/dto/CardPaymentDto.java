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
    private String[] requireKey = {"cardNumber", "expiryDate", "cvc", "installment", "amount"};

    public CardPaymentDto() {}

    @Override
    public void checkKey(JsonObject obj) {
        for (String key : this.requireKey) {
            if (!obj.has(key)) {
                throw new CustomException(ErrorCode.NOT_EXISTS_REQUIRED_KEY);
            }
        }
    }

    public CardPayment transferBody(String body) {
        Gson gson = new Gson();
        JsonParser p = new JsonParser();
        JsonObject obj = (JsonObject) p.parse(body);
        checkKey(obj);
        checkValue(obj);

        //Json으로 받은 요청에 vat 필드가 없다면 -1로 셋팅하고 기본 자동계산
        CardPayment cardPayment = gson.fromJson(body, CardPayment.class);
        if (!obj.has("vat")) {
            cardPayment.setVat(-1);
        }
        return cardPayment;
    }

    private void checkValue(JsonObject obj) {
        checkStringValue(obj.get("cardNumber").getAsString(), "^[0-9]{10,16}");
        checkStringValue(obj.get("expiryDate").getAsString(), "^[0-9]{4,4}");
        checkStringValue(obj.get("cvc").getAsString(), "^[0-9]{3,3}");
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
