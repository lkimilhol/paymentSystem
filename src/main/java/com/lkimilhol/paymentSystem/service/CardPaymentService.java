package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.CardPayment;
import com.lkimilhol.paymentSystem.repository.CardPaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CardPaymentService {
    private CardPaymentRepository cardPaymentRepository;

    public CardPaymentService(CardPaymentRepository cardPaymentRepository) {
        this.cardPaymentRepository = cardPaymentRepository;
    }

    public CardPayment save(CardPayment cardPayment) {
        cardPayment.setInsertTime(LocalDateTime.now());
        cardPayment.setPaymentStatus(true);
        cardPaymentRepository.save(cardPayment);
        return cardPayment;
    }

    public Optional<CardPayment> findByUniqueId(String uniqueId) {
        return cardPaymentRepository.findByUniqueId(uniqueId);
    }
}
