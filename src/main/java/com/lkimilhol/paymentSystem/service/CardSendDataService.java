package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.CardSendData;
import com.lkimilhol.paymentSystem.repository.CardSendDataRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class CardSendDataService {
    private CardSendDataRepository cardSendDataRepository;

    public CardSendDataService(CardSendDataRepository cardSendDataRepository) {
        this.cardSendDataRepository = cardSendDataRepository;
    }

    public CardSendData save(String uniqueId, String data) {
        CardSendData cardSendData = new CardSendData();
        cardSendData.setUniqueId(uniqueId);
        cardSendData.setCardData(data);
        cardSendData.setSendTime(LocalDateTime.now());
        cardSendDataRepository.save(cardSendData);
        return cardSendData;
    }

    public Optional<CardSendData> findByUniqueId(String uniqueId) {
        return cardSendDataRepository.findByUniqueId(uniqueId);
    }
}
