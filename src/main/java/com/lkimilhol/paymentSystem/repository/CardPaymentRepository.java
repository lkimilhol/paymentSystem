package com.lkimilhol.paymentSystem.repository;

import com.lkimilhol.paymentSystem.domain.CardAdmin;
import com.lkimilhol.paymentSystem.domain.CardPayment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

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

    public Optional<CardPayment> findByUniqueId(String uniqueId) {
        List<CardPayment> result = em.createQuery("select t from CardPayment t where t.uniqueId = :uniqueId", CardPayment.class)
                .setParameter("uniqueId", uniqueId)
                .getResultList();

        return result.stream().findAny();
    }
}
