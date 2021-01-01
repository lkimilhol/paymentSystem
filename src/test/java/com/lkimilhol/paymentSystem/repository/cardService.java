package com.lkimilhol.paymentSystem.repository;

import com.lkimilhol.paymentSystem.domain.Card;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Rollback
public class cardService {

    @Autowired
    private CardRepository cardRepository;

    @Test
    public void 카드_생성() {
        Card card = new Card();
        card.setExpiryDate("0912");
        card.setCsv("198");
        cardRepository.save(card);
        assertTrue(card.getCardId() > 0);
    }
}
