package com.lkimilhol.paymentSystem.dto;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lkimilhol.paymentSystem.domain.CardCancel;
import com.lkimilhol.paymentSystem.domain.CardPayment;
import com.lkimilhol.paymentSystem.domain.CardSendData;
import com.lkimilhol.paymentSystem.global.error.CustomException;
import com.lkimilhol.paymentSystem.global.error.ErrorCode;


public class CardCancelDto implements DataTransferObjectService {
    private String[] requireKey = {"uniqueId", "amount"};

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

        //Json으로 받은 요청에 vat 필드가 없다면 -1로 셋팅하고 기본 자동계산
        CardCancel cardCancel = gson.fromJson(body, CardCancel.class);
        if (!obj.has("vat")) {
            cardCancel.setVat(-1);
        }

        return cardCancel;
    }
}
