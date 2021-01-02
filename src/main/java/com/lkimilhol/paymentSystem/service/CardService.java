package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.Card;
import com.lkimilhol.paymentSystem.domain.CardPayment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CardService {

    public String pay(Card card) {
        System.out.println(card.toString());
        return "";
    }

    private int calculateVat(int amount, int vat) {
        double doubleVat = 0d;
        vat = vat == 0 ? 11 : vat;

        amount = Math.round((float) amount / vat);
        return amount;
    }

//    public Optional<CardPayment> findById(long id) {
//        return cardRepository.findById(id);
//    }

//    public List<CardPayment> findCardList() {
//        return cardRepository.findAll();
//    }
}
