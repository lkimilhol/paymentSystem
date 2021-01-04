package com.lkimilhol.paymentSystem.repository;

import com.lkimilhol.paymentSystem.domain.CardCancel;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class CardCancelRepository {
    public CardCancelRepository(EntityManager em) {
        this.em = em;
    }
    private final EntityManager em;

    public CardCancel save(CardCancel cardCancel) {
        em.persist(cardCancel);
        return cardCancel;
    }

    public Optional<CardCancel> findByUniqueId(String uniqueId) {
        List<CardCancel> result = em.createQuery("select t from CardCancel t where t.uniqueId = :uniqueId", CardCancel.class)
                .setParameter("uniqueId", uniqueId)
                .getResultList();

        return result.stream().findAny();
    }
}
