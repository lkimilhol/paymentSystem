package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.CardPayment;
import com.lkimilhol.paymentSystem.global.common.CommonUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;

@SpringBootTest
@Rollback
public class CardServiceTest {
    @Autowired
    CardService cardService;

    @Test
    @DisplayName("카드 결제 API 테스트")
    public void pay() {
        //given
        CardPayment cardPayment = new CardPayment();
        cardPayment.setCardNumber("1234567890");
        cardPayment.setInstallment(0);
        cardPayment.setCvc("981");
        cardPayment.setExpiryDate("1112");
        cardPayment.setAmount(10000);
        cardPayment.setVat(0);
        cardPayment.setInsertTime(LocalDateTime.now());

        //when
        cardService.pay(cardPayment);

        //then
        Assertions.assertTrue(cardPayment.getSeq() > 0);
    }
}