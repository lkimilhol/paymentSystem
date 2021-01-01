package com.lkimilhol.paymentSystem.repository;

import com.lkimilhol.paymentSystem.domain.Card;
import com.lkimilhol.paymentSystem.service.CardService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Rollback
public class cardService {

    @Autowired
    private CardService cardService;

    @Autowired
    CardRepository cardRepository;

    @AfterEach
    public void cleanAll() {
        cardRepository.deleteAll();
    }

    @Test
    public void 카드_생성() {
        //given
        Card card = new Card();
        card.setExpiryDate("0912");
        card.setCsv("198");

        //when
        cardService.add(card);

        //then
        assertTrue(card.getId() > 0);
    }

    @Test
    public void 카드_불러오기() {
        //given
        Card card = new Card();
        card.setExpiryDate("0912");
        card.setCsv("198");
        cardService.add(card);

        //when
        long cardId = card.getId();
        Optional<Card> insertedCard = cardService.findById(cardId);

        //then
        assertEquals(insertedCard.get().getId(), cardId);
    }

    @Test
    public void 카드_전체목록() {
        //given
        Card card = new Card();
        card.setExpiryDate("0912");
        card.setCsv("198");
        cardService.add(card);

        Card card2 = new Card();
        card2.setExpiryDate("1209");
        card2.setCsv("123");
        cardService.add(card2);

        //when
        List<Card> cardList = cardService.findCardList();

        //then
        System.out.println(cardList.size());
        assertTrue(cardList.size() == 2);
    }
}
