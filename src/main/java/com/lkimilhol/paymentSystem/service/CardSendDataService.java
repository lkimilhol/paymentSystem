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

    public CardSendData save(String data) {
        CardSendData cardSendData = new CardSendData();
        cardSendData.setCardData(data);
        cardSendData.setSendTime(LocalDateTime.now());
        cardSendDataRepository.save(cardSendData);
        return cardSendData;
    }
}
