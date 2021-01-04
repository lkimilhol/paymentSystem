package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.CardCancel;
import com.lkimilhol.paymentSystem.domain.CardPayment;
import com.lkimilhol.paymentSystem.dto.CardPaymentDto;
import com.lkimilhol.paymentSystem.global.error.CustomException;
import com.lkimilhol.paymentSystem.global.error.ErrorCode;
import com.lkimilhol.paymentSystem.responseApi.CardCancelResponse;
import com.lkimilhol.paymentSystem.responseApi.CardGetResponse;
import com.lkimilhol.paymentSystem.responseApi.CardPaymentResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;

@SpringBootTest
@Rollback
public class CardServiceTest {
    @Autowired
    private CardService cardService;

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
        cardCancel.setAmount(cardPayment.getAmount());
        cardCancel.setVat(cardPayment.getVat());

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

    @Test
    @DisplayName("부가세 계산")
    public void calculateVat() {
        //given
        CardPayment cardPayment = new CardPayment();
        cardPayment.setCardNumber("1234567890");
        cardPayment.setInstallment(0);
        cardPayment.setCvc("981");
        cardPayment.setExpiryDate("1112");
        cardPayment.setAmount(10000);
        cardPayment.setVat(-1);
        cardService.pay(cardPayment);

        //when
        cardService.get(cardPayment.getUniqueId());

        //then
        Assertions.assertEquals(10000 / 11, cardPayment.getVat());
    }

    @Test
    @DisplayName("부가세 다를 경우")
    public void invalidCalculateVat() {
        //given
        CardPayment cardPayment = new CardPayment();
        cardPayment.setCardNumber("1234567890");
        cardPayment.setInstallment(0);
        cardPayment.setCvc("981");
        cardPayment.setExpiryDate("1112");
        cardPayment.setAmount(10000);
        cardPayment.setVat(0);
        cardService.pay(cardPayment);
        CardGetResponse cardGetResponse = cardService.get(cardPayment.getUniqueId());

        CardCancel cardCancel = new CardCancel();
        cardCancel.setUniqueId(cardGetResponse.getUniqueId());
        cardCancel.setAmount(10000);
        cardCancel.setVat(10);

        //when
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            cardService.cancel(cardCancel);
        });

        //then
        Assertions.assertEquals(ErrorCode.NOT_EQUAL_TOTAL_AMOUNT, exception.getErrorCode());
    }
}