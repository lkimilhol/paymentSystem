package com.lkimilhol.paymentSystem.repository;

import com.lkimilhol.paymentSystem.domain.Card;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
public class cardService {

    @Autowired
    private EntityManager em;


    @Test
    public void 카드_생성() {
        CardRepository cardRepository = new CardRepository(em);
        Card card = new Card();
        card.setExpiryDate("0912");
        card.setCsv("198");
        cardRepository.save(card);
        assertTrue(card.getCardId() > 0);
    }
}
