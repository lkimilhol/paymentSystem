package com.lkimilhol.paymentSystem.dto;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lkimilhol.paymentSystem.domain.CardCancel;
import com.lkimilhol.paymentSystem.domain.CardSendData;
import com.lkimilhol.paymentSystem.global.error.CustomException;
import com.lkimilhol.paymentSystem.global.error.ErrorCode;


public class CardCancelDto implements DataTransferObjectService {
    private String[] requireKey = {"uniqueId, amount"};

    @Override
    public void checkKey(JsonObject obj) {
        for (String key : this.requireKey) {
            if (!obj.has(key)) {
                throw new CustomException(ErrorCode.NOT_EXISTS_REQUIRED_KEY);
            }
        }
    }

    public CardCancel transferBody(String body) {
        Gson gson = new Gson();
        JsonParser p = new JsonParser();
        JsonObject obj = (JsonObject) p.parse(body);
        checkKey(obj);

        return gson.fromJson(body, CardCancel.class);
    }
}
