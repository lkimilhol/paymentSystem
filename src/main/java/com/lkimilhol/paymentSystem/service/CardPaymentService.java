package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.CardPayment;
import com.lkimilhol.paymentSystem.repository.CardPaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CardPaymentService {
    private CardPaymentRepository cardPaymentRepository;

    public CardPaymentService(CardPaymentRepository cardPaymentRepository) {
        this.cardPaymentRepository = cardPaymentRepository;
    }

    public CardPayment save(CardPayment cardPayment) {
        cardPayment.setVat(calculateVat(cardPayment.getAmount(), cardPayment.getVat()));
        cardPayment.setInsertTime(LocalDateTime.now());
        cardPayment.setPaymentStatus(true);
        cardPaymentRepository.save(cardPayment);
        return cardPayment;
    }

    private int calculateVat(int amount, int vat) {
        return vat == 0 ? Math.round((float) amount / 11) : vat;
    }
}
