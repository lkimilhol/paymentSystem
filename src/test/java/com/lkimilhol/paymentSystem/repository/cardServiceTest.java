//package com.lkimilhol.paymentSystem.repository;
//
//import com.lkimilhol.paymentSystem.domain.CardPayment;
//import com.lkimilhol.paymentSystem.service.CardService;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@SpringBootTest
//@Rollback
//public class cardServiceTest {
//
//    @Autowired
//    private CardService cardService;
//
//    @Autowired
//    CardRepository cardRepository;
//
//    @AfterEach
//    public void cleanAll() {
//        cardRepository.deleteAll();
//    }
//
//    @Test
//    public void 카드_생성() {
//        //given
//        CardPayment cardPayment = new CardPayment();
//        cardPayment.setExpiryDate("0912");
//        cardPayment.setCsv("198");
//
//        //when
//        cardService.add(cardPayment);
//
//        //then
//        assertTrue(cardPayment.getId() > 0);
//    }
//
//    @Test
//    public void 카드_불러오기() {
//        //given
//        CardPayment cardPayment = new CardPayment();
//        cardPayment.setExpiryDate("0912");
//        cardPayment.setCsv("198");
//        cardService.add(cardPayment);
//
//        //when
//        long cardId = cardPayment.getId();
//        Optional<CardPayment> insertedCard = cardService.findById(cardId);
//
//        //then
//        assertEquals(insertedCard.get().getId(), cardId);
//    }
//
//    @Test
//    public void 카드_전체목록() {
//        //given
//        CardPayment cardPayment = new CardPayment();
//        cardPayment.setExpiryDate("0912");
//        cardPayment.setCsv("198");
//        cardService.add(cardPayment);
//
//        CardPayment cardPayment2 = new CardPayment();
//        cardPayment2.setExpiryDate("1209");
//        cardPayment2.setCsv("123");
//        cardService.add(cardPayment2);
//
//        //when
//        List<CardPayment> cardPaymentList = cardService.findCardList();
//
//        //then
//        System.out.println(cardPaymentList.size());
//        assertTrue(cardPaymentList.size() == 2);
//    }
//}
