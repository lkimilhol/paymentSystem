package com.lkimilhol.paymentSystem.controller;


import com.google.gson.Gson;
import com.lkimilhol.paymentSystem.domain.Card;
import com.lkimilhol.paymentSystem.service.CardService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    private final Gson gson = new Gson();

    /*
        카드 번호를 받을 때 numeric 으로 받을 때 시작이 0인 경우 에러가 나므로 string으로 받아야 함
     */
    @RequestMapping(value = "/card/new", method = RequestMethod.POST, produces = "application/json; charset=utf8")
    @ResponseBody
    public Card addCard(@RequestBody Card card) {
        gson.toJson(card);
        cardService.add(card);
        return card;
    }
}
