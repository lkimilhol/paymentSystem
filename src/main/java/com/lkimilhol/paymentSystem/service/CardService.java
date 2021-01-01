package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.Card;
import com.lkimilhol.paymentSystem.repository.CardRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CardService {
    private CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Long add(Card card) {
        cardRepository.save(card);
        return card.getId();
    }

    public Optional<Card> findById(long id) {
        return cardRepository.findById(id);
    }

    public List<Card> findCardList() {
        return cardRepository.findAll();
    }
}
