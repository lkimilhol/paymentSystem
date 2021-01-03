package com.lkimilhol.paymentSystem.dto;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lkimilhol.paymentSystem.domain.Card;
import com.lkimilhol.paymentSystem.global.error.CustomException;
import com.lkimilhol.paymentSystem.global.error.ErrorCode;
import lombok.*;


@Data

public class CardDto implements DataTransferObjectService {
    private String[] requireKey = {"cardNumber", "expiryDate", "csv", "installment", "amount"};

    public CardDto() {}

    @Override
    public void keyCheck(JsonObject obj) {
        for (String s : this.requireKey) {
            if (!obj.has(s)) {
                throw new CustomException(ErrorCode.NOT_EXISTS_REQUIRED_KEY);
            }
        }
    }

    public Card transferCard(String s) {
        Gson gson = new Gson();
        JsonParser p = new JsonParser();
        JsonObject obj = (JsonObject) p.parse(s);
        keyCheck(obj);

        return gson.fromJson(s, Card.class);
    }

}
