package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.CardCancel;
import com.lkimilhol.paymentSystem.domain.CardPayment;
import com.lkimilhol.paymentSystem.global.error.CustomException;
import com.lkimilhol.paymentSystem.global.error.ErrorCode;
import com.lkimilhol.paymentSystem.responseApi.CardCancelResponse;
import com.lkimilhol.paymentSystem.responseApi.CardPaymentResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;

@SpringBootTest
public class ScenarioTest3 {
    @Autowired
    private CardApiService cardApiService;

    private static String uniqueId;

    @BeforeAll
    static void setUp() {
        uniqueId = "";
    }

    @Order(1)
    @Test
    @DisplayName("시나리오 3-1 테스트 - 20000원 결제")
    public void scenario1() {
        //given
        CardPayment cardPayment = new CardPayment();
        cardPayment.setCardNumber("1234567890");
        cardPayment.setExpiryDate("1188");
        cardPayment.setCvc("098");
        cardPayment.setAmount(20000);
        cardPayment.setVat(-1);
        cardPayment.setInstallment(0);

        //when
        CardPaymentResponse cardPaymentResponse = cardApiService.pay(cardPayment);

        //then
        uniqueId = cardPaymentResponse.getUniqueId();
        Assertions.assertEquals(HttpStatus.OK, cardPaymentResponse.getStatus());
    }

    @Order(2)
    @Test
    @DisplayName("시나리오 3-2 테스트 - 10000원 1000 결제")
    public void scenario2() {
        //given
        CardCancel cardCancel = new CardCancel();
        cardCancel.setUniqueId(uniqueId);
        cardCancel.setAmount(10000);
        cardCancel.setVat(1000);
        cardCancel.setPartCancel(true);

        //when
        CardCancelResponse cardCancelResponse = cardApiService.cancel(cardCancel);

        //then
        Assertions.assertEquals(HttpStatus.OK, cardCancelResponse.getStatus());
    }

    @Order(3)
    @Test
    @DisplayName("시나리오 3-3 테스트 - 10000원 909원 취소 실패")
    public void scenario3() {
        //given
        CardCancel cardCancel = new CardCancel();
        cardCancel.setUniqueId(uniqueId);
        cardCancel.setAmount(10000);
        cardCancel.setVat(909);
        cardCancel.setPartCancel(true);

        //when
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            cardApiService.cancel(cardCancel);
        });

        //then
        Assertions.assertEquals(ErrorCode.INVALID_PAY_VAT, exception.getErrorCode());
    }

    @Order(4)
    @Test
    @DisplayName("시나리오 3-4 테스트 - 10000 취소")
    public void scenario4() {
        //given
        CardCancel cardCancel = new CardCancel();
        cardCancel.setUniqueId(uniqueId);
        cardCancel.setAmount(10000);
        cardCancel.setVat(-1);
        cardCancel.setPartCancel(true);

        //when
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            cardApiService.cancel(cardCancel);
        });

        //then
        Assertions.assertEquals(ErrorCode.INVALID_PAY_VAT, exception.getErrorCode());
    }
}