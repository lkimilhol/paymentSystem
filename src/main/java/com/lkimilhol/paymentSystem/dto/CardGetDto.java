package com.lkimilhol.paymentSystem.dto;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lkimilhol.paymentSystem.domain.CardSendData;
import com.lkimilhol.paymentSystem.global.error.CustomException;
import com.lkimilhol.paymentSystem.global.error.ErrorCode;

public class CardGetDto implements DataTransferObjectService {
    private String[] requireKey = {"uniqueId"};

    @Override
    public void checkKey(JsonObject obj) {
        for (String key : this.requireKey) {
            if (!obj.has(key)) {
                throw new CustomException(ErrorCode.NOT_EXISTS_REQUIRED_KEY);
            }
        }
    }

    public CardSendData transferBody(String body) {
        Gson gson = new Gson();
        JsonParser p = new JsonParser();
        JsonObject obj = (JsonObject) p.parse(body);
        checkKey(obj);

        return gson.fromJson(body, CardSendData.class);
    }
}
