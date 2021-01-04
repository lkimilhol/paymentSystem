package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.CardCancel;
import com.lkimilhol.paymentSystem.domain.CardPayment;
import com.lkimilhol.paymentSystem.repository.CardCancelRepository;
import com.lkimilhol.paymentSystem.repository.CardPaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CardCancelService {
    private CardCancelRepository cardCancelRepository;


    public CardCancelService(CardCancelRepository cardCancelRepository) {
        this.cardCancelRepository = cardCancelRepository;
    }

    public CardCancel save(CardCancel cardCancel) {
        cardCancel.setInsertTime(LocalDateTime.now());
        cardCancelRepository.save(cardCancel);
        return cardCancel;
    }

    public Optional<CardCancel> findByUniqueId(String uniqueId) {
        return cardCancelRepository.findByUniqueId(uniqueId);
    }
}
