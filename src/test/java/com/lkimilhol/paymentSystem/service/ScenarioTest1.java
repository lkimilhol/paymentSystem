package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.CardCancel;
import com.lkimilhol.paymentSystem.domain.CardPayment;
import com.lkimilhol.paymentSystem.global.error.CustomException;
import com.lkimilhol.paymentSystem.global.error.ErrorCode;
import com.lkimilhol.paymentSystem.responseApi.CardCancelResponse;
import com.lkimilhol.paymentSystem.responseApi.CardPaymentResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;


@SpringBootTest
public class ScenarioTest1 {
    @Autowired
    private CardApiService cardApiService;

    private static String uniqueId;

    @BeforeAll
    static void setUp() {
        uniqueId = "";
    }

    @Order(1)
    @Test
    @DisplayName("시나리오 1-1 테스트 - 11000원 1000원 결제")
    public void scenario1() {
        //given
        CardPayment cardPayment = new CardPayment();
        cardPayment.setCardNumber("1234567890");
        cardPayment.setExpiryDate("1188");
        cardPayment.setCvc("098");
        cardPayment.setAmount(11000);
        cardPayment.setVat(1000);
        cardPayment.setInstallment(0);

        //when
        CardPaymentResponse cardPaymentResponse = cardApiService.pay(cardPayment);

        //then
        uniqueId = cardPaymentResponse.getUniqueId();
        Assertions.assertEquals(HttpStatus.OK, cardPaymentResponse.getStatus());
    }

    @Order(2)
    @Test
    @DisplayName("시나리오 1-2 테스트 - 1100원 100원 취소")
    public void scenario2() {
        //given
        CardCancel cardCancel = new CardCancel();
        cardCancel.setUniqueId(uniqueId);
        cardCancel.setAmount(1100);
        cardCancel.setVat(100);
        cardCancel.setPartCancel(true);

        //when
        CardCancelResponse cardCancelResponse = cardApiService.cancel(cardCancel);

        //then
        Assertions.assertEquals(HttpStatus.OK, cardCancelResponse.getStatus());
    }

    @Order(3)
    @Test
    @DisplayName("시나리오 1-3 테스트 - 3300원 취소")
    public void scenario3() {
        //given
        CardCancel cardCancel = new CardCancel();
        cardCancel.setUniqueId(uniqueId);
        cardCancel.setAmount(3300);
        cardCancel.setVat(-1);
        cardCancel.setPartCancel(true);

        //when
        CardCancelResponse cardCancelResponse = cardApiService.cancel(cardCancel);

        //then
        Assertions.assertEquals(HttpStatus.OK, cardCancelResponse.getStatus());
    }

    @Order(4)
    @Test
    @DisplayName("시나리오 1-4 테스트 - 7000 취소 실패")
    public void scenario4() {
        //given
        CardCancel cardCancel = new CardCancel();
        cardCancel.setUniqueId(uniqueId);
        cardCancel.setAmount(7000);
        cardCancel.setVat(-1);
        cardCancel.setPartCancel(true);

        //when
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            cardApiService.cancel(cardCancel);
        });

        //then
        Assertions.assertEquals(ErrorCode.NOT_ENOUGH_PAY_AMOUNT, exception.getErrorCode());
    }

    @Order(5)
    @Test
    @DisplayName("시나리오 1-5 테스트 - 6600원 700원 취소 실패")
    public void scenario5() {
        //given
        CardCancel cardCancel = new CardCancel();
        cardCancel.setUniqueId(uniqueId);
        cardCancel.setAmount(6600);
        cardCancel.setVat(700);
        cardCancel.setPartCancel(true);

        //when
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            cardApiService.cancel(cardCancel);
        });

        //then
        Assertions.assertEquals(ErrorCode.INVALID_PAY_VAT, exception.getErrorCode());
    }

    @Order(6)
    @Test
    @DisplayName("시나리오 1-6 테스트 - 6600원 600원 취소")
    public void scenario6() {
        //given
        CardCancel cardCancel = new CardCancel();
        cardCancel.setUniqueId(uniqueId);
        cardCancel.setAmount(6600);
        cardCancel.setVat(600);
        cardCancel.setPartCancel(true);

        //when
        CardCancelResponse cardCancelResponse = cardApiService.cancel(cardCancel);

        //then
        Assertions.assertEquals(HttpStatus.OK, cardCancelResponse.getStatus());
    }

    @Order(7)
    @Test
    @DisplayName("시나리오 1-7 테스트 - 100원 취소 실패")
    public void scenario7() {
        //given
        CardCancel cardCancel = new CardCancel();
        cardCancel.setUniqueId(uniqueId);
        cardCancel.setAmount(100);
        cardCancel.setVat(-1);
        cardCancel.setPartCancel(true);

        //when
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            cardApiService.cancel(cardCancel);
        });

        //then
        Assertions.assertEquals(ErrorCode.ALREADY_CANCEL, exception.getErrorCode());
    }
}