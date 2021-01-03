package com.lkimilhol.paymentSystem.controller;


import com.lkimilhol.paymentSystem.domain.CardSendData;
import com.lkimilhol.paymentSystem.dto.CardGetDto;
import com.lkimilhol.paymentSystem.dto.CardPaymentDto;
import com.lkimilhol.paymentSystem.responseApi.CardGetResponse;
import com.lkimilhol.paymentSystem.responseApi.CardPaymentResponse;
import com.lkimilhol.paymentSystem.service.CardService;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "/card/pay", method = RequestMethod.POST, produces = "application/json; charset=utf8")
    @ResponseBody
    public ResponseEntity<CardPaymentResponse> pay(@RequestBody String body) {
        CardPaymentDto dto = new CardPaymentDto();
        CardPaymentResponse cardPaymentResponse = cardService.pay(dto.transferCard(body));
        return ResponseEntity.ok(cardPaymentResponse);
    }

    @RequestMapping(value = "/card/get", method = RequestMethod.GET, produces = "application/json; charset=utf8")
    @ResponseBody
    public ResponseEntity<CardGetResponse> get(@RequestBody String body) {
        CardGetDto dto = new CardGetDto();
        CardSendData cardSendData = dto.transferBody(body);
        CardGetResponse cardGetResponse = cardService.get(cardSendData.getUniqueId());
        return ResponseEntity.ok(cardGetResponse);
    }
}
