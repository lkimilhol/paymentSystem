package com.lkimilhol.paymentSystem.repository;

import com.lkimilhol.paymentSystem.domain.CardAdmin;
import com.lkimilhol.paymentSystem.domain.CardSendData;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class CardAdminRepository {
    public CardAdminRepository(EntityManager em) {
        this.em = em;
    }
    private final EntityManager em;

    public CardAdmin save(CardAdmin cardAdmin) {
        em.persist(cardAdmin);
        return cardAdmin;
    }

    public Optional<CardAdmin> findByUniqueId(String uniqueId) {
        List<CardAdmin> result = em.createQuery("select t from CardAdmin t where t.uniqueId = :uniqueId", CardAdmin.class)
                .setParameter("uniqueId", uniqueId)
                .getResultList();

        return result.stream().findAny();
    }
}
