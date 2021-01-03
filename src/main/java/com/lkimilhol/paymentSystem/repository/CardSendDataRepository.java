package com.lkimilhol.paymentSystem.repository;

import com.lkimilhol.paymentSystem.domain.CardPayment;
import com.lkimilhol.paymentSystem.domain.CardSendData;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class CardSendDataRepository {
    public CardSendDataRepository(EntityManager em) {
        this.em = em;
    }

    private final EntityManager em;

    public CardSendData save(CardSendData cardSendData) {
        em.persist(cardSendData);
        return cardSendData;
    }
}