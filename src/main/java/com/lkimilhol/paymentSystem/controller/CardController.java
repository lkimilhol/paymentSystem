package com.lkimilhol.paymentSystem.controller;


import com.google.gson.Gson;
import com.lkimilhol.paymentSystem.dto.CardDto;
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

    @RequestMapping(value = "/card/pay", method = RequestMethod.POST, produces = "application/json; charset=utf8")
    @ResponseBody
    public String pay(@RequestBody String body) {
        CardDto dto = new CardDto();
        cardService.pay(dto.transferCard(body));
        return "";
    }
}
