package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.Card;
import com.lkimilhol.paymentSystem.repository.CardRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CardService {
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Long add(Card card) {
        cardRepository.save(card);
        return card.getCardId();
    }

    public List<Card> findCardList() {
        return cardRepository.findAll();
    }
}
