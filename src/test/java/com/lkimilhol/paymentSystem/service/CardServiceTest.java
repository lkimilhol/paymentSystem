package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.CardAdmin;
import com.lkimilhol.paymentSystem.domain.CardCancel;
import com.lkimilhol.paymentSystem.domain.CardPayment;
import com.lkimilhol.paymentSystem.global.common.CommonUtility;
import com.lkimilhol.paymentSystem.global.error.CustomException;
import com.lkimilhol.paymentSystem.global.error.ErrorCode;
import com.lkimilhol.paymentSystem.repository.CardPaymentRepository;
import com.lkimilhol.paymentSystem.responseApi.CardCancelResponse;
import com.lkimilhol.paymentSystem.responseApi.CardGetResponse;
import com.lkimilhol.paymentSystem.responseApi.CardPaymentResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@SpringBootTest
@Rollback
public class CardServiceTest {
    @Autowired
    private CardService cardService;
//    private CardPaymentService cardPaymentService;
//    private SubjectService subjectService;
//    private ScoreService scoreService;

    @Autowired
    private CardAdminService cardAdminService;

    @Test
    @DisplayName("카드 결제 API")
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
        CardPaymentResponse cardPaymentResponse = cardService.pay(cardPayment);

        //then
        Assertions.assertEquals(HttpStatus.OK, cardPaymentResponse.getStatus());
    }

    @Test
    @DisplayName("카드 취소 API")
    public void cancel() {
        //given
        CardPayment cardPayment = new CardPayment();
        cardPayment.setCardNumber("1234567890");
        cardPayment.setInstallment(0);
        cardPayment.setCvc("981");
        cardPayment.setExpiryDate("1112");
        cardPayment.setAmount(10000);
        cardPayment.setVat(5);
        cardService.pay(cardPayment);

        CardCancel cardCancel = new CardCancel();
        cardCancel.setUniqueId(cardPayment.getUniqueId());
        cardCancel.setAmount(cardPayment.getAmount() + cardPayment.getVat());

        //when
        CardCancelResponse cardCancelResponse = cardService.cancel(cardCancel);

        //then
        Assertions.assertEquals(HttpStatus.OK, cardCancelResponse.getStatus());
    }

    @Test
    @DisplayName("카드 정보 API")
    public void get() {
        //given
        CardPayment cardPayment = new CardPayment();
        cardPayment.setCardNumber("1234567890");
        cardPayment.setInstallment(0);
        cardPayment.setCvc("981");
        cardPayment.setExpiryDate("1112");
        cardPayment.setAmount(10000);
        cardPayment.setVat(5);
        cardService.pay(cardPayment);

        //when
        CardGetResponse cardGetResponse = cardService.get(cardPayment.getUniqueId());

        //then
        Assertions.assertEquals(cardGetResponse.getUniqueId(), cardGetResponse.getUniqueId());
        Assertions.assertEquals("123456*890",cardGetResponse.getCardNum());
        Assertions.assertEquals(cardPayment.getAmount(), cardGetResponse.getAmount());
    }
}