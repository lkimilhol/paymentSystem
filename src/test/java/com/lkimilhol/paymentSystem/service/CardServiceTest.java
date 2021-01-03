package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.CardPayment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class CardServiceTest {
    @Autowired
    CardService cardService;

    @Test
    @DisplayName("카드 결제 API 테스")
    public void cardData() {
        //given
        CardPayment cardPayment = new CardPayment();
        cardPayment.setCardNumber(1234567890);
        cardPayment.setInstallment(0);
        cardPayment.setCsv(981);
        cardPayment.setAmount(10000);
        cardPayment.setVat(0);
        cardPayment.setInsertTime(LocalDateTime.now());

        //when
        cardService.pay(cardPayment);

        //then

    }
}
