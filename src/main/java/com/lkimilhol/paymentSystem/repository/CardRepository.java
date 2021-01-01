package com.lkimilhol.paymentSystem.repository;

import com.lkimilhol.paymentSystem.domain.Card;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class CardRepository {
    private final EntityManager em;

    public CardRepository(EntityManager em) {
        this.em = em;
    }

    public Card save(Card card) {
        em.persist(card);
        return card;
    }

    public List<Card> findAll() {
        return em.createQuery("select c from Card c ", Card.class)
                .getResultList();
    }
}
