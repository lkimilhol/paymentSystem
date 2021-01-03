package com.lkimilhol.paymentSystem.controller;


import com.lkimilhol.paymentSystem.domain.CardAdmin;
import com.lkimilhol.paymentSystem.domain.CardCancel;
import com.lkimilhol.paymentSystem.domain.CardSendData;
import com.lkimilhol.paymentSystem.dto.CardCancelDto;
import com.lkimilhol.paymentSystem.dto.CardGetDto;
import com.lkimilhol.paymentSystem.dto.CardPaymentDto;
import com.lkimilhol.paymentSystem.responseApi.CardCancelResponse;
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
        CardPaymentResponse cardPaymentResponse = cardService.pay(dto.transferBody(body));
        return ResponseEntity.ok(cardPaymentResponse);
    }

    @RequestMapping(value = "/card/get", method = RequestMethod.GET, produces = "application/json; charset=utf8")
    @ResponseBody
    public ResponseEntity<CardGetResponse> get(@RequestBody String body) {
        CardGetDto dto = new CardGetDto();
        CardAdmin cardAdmin = dto.transferBody(body);
        CardGetResponse cardGetResponse = cardService.get(cardAdmin.getUniqueId());
        return ResponseEntity.ok(cardGetResponse);
    }

    @RequestMapping(value = "/card/cancel", method = RequestMethod.POST, produces = "application/json; charset=utf8")
    @ResponseBody
    public ResponseEntity<CardCancelResponse> cancel(@RequestBody String body) {
        CardCancelDto dto = new CardCancelDto();
        CardCancel cardCancel = dto.transferBody(body);
        CardCancelResponse cardCancelResponse = cardService.cancel(cardCancel);
        return ResponseEntity.ok(cardCancelResponse);
    }
}
