package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.CardPayment;
import com.lkimilhol.paymentSystem.domain.CardSendData;
import com.lkimilhol.paymentSystem.repository.CardPaymentRepository;
import com.lkimilhol.paymentSystem.repository.CardSendDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class CardService {

    private CardPaymentRepository cardPaymentRepository;
    private CardSendDataRepository cardSendDataRepository;

    public CardService(CardPaymentRepository cp, CardSendDataRepository cs) {
        this.cardPaymentRepository = cp;
        this.cardSendDataRepository = cs;
    }


    public String pay(CardPayment cardPayment) {
        cardPayment.setInsertTime(LocalDateTime.now());
        cardPaymentRepository.save(cardPayment);
//        long cardPaymentId = cardPayment.getUniqueId();

        int vatAmount = calculateVat(cardPayment.getAmount(), cardPayment.getVat());
        cardPayment.setAmount(cardPayment.getAmount() + vatAmount);


        return "";
    }

    private int calculateVat(int amount, int vat) {
        amount += vat == 0 ? Math.round((float) amount / 11) : vat;
        return amount;
    }

    private String encrypt(CardPayment cardPayment) {

        return "";
    }

//    public Optional<CardPayment> findById(long id) {
//        return cardRepository.findById(id);
//    }

//    public List<CardPayment> findCardList() {
//        return cardRepository.findAll();
//    }
}
