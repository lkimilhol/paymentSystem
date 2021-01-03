package com.lkimilhol.paymentSystem.repository;

import com.lkimilhol.paymentSystem.domain.CardPayment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class CardPaymentRepository {
    public CardPaymentRepository(EntityManager em) {
        this.em = em;
    }
    private final EntityManager em;

    public CardPayment save(CardPayment cardPayment) {
        em.persist(cardPayment);
        return cardPayment;
    }
}
