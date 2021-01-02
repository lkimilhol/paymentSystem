package com.lkimilhol.paymentSystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lkimilhol.paymentSystem.domain.Card;
import com.lkimilhol.paymentSystem.domain.CardPayment;
import lombok.*;

import javax.swing.text.Element;

@Data
public class CardDto implements DataTransferObjectService {
    private String[] requireKey = {"cardNumber", "expiryDate", "csv", "installment", "amount"};

    public CardDto() {}

    @Override
    public void keyCheck(JsonObject obj) {
        for (String s : this.requireKey) {
            if (!obj.has(s)) {
                System.out.println("out!");
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
