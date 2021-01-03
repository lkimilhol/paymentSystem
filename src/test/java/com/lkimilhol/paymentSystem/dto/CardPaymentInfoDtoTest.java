package com.lkimilhol.paymentSystem.dto;

import com.lkimilhol.paymentSystem.domain.CardPayment;
import com.lkimilhol.paymentSystem.global.error.CustomException;
import com.lkimilhol.paymentSystem.global.error.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CardPaymentInfoDtoTest {

    @Test
    @DisplayName("정상 동작")
    void cardKeyCheck() {
        // given
        String test = "{\"cardNumber\":\"1234567890\", \"expiryDate\": \"0123\"," +
                "\"csv\":\"098\", \"installment\": 0," +
                "\"amount\":10000, \"vat\": 0}";

        // when
        CardPaymentDto dto = new CardPaymentDto();
        CardPayment cardPayment = dto.transferCard(test);

        // then
        assertEquals(cardPayment.getCardNumber(), "1234567890");
        assertEquals(cardPayment.getExpiryDate(), "0123");
        assertEquals(cardPayment.getCsv(), "098");
        assertEquals(cardPayment.getInstallment(), 0);
        assertEquals(cardPayment.getAmount(), 10000);
        assertEquals(cardPayment.getVat(), 0);
    }


    @Test()
    @DisplayName("필수키 없음 에러")
    void cardRequiredKeyError() {
        // given
        String test = "{\"cardNumber\":\"1234567890\", \"expiryDate\": \"0123\"," +
                "\"csv\":\"098\", \"installment\": 0," +
                "vat\": 0}";

        // when
        CardPaymentDto dto = new CardPaymentDto();
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            CardPayment cardPayment = dto.transferCard(test);
        });

        // then
        Assertions.assertEquals(ErrorCode.NOT_EXISTS_REQUIRED_KEY, exception.getErrorCode());
    }

    @Test()
    @DisplayName("잘못 된 값 체크")
    void cardPaymentValueError() {
        // given
        String test = "{\"cardNumber\":\"1234567890AAAA\", \"expiryDate\": \"0123\"," +
                "\"csv\":\"098\", \"installment\": 0," +
                "\"amount\":10000, \"vat\": 0}";

        // when
        CardPaymentDto dto = new CardPaymentDto();
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            CardPayment cardPayment = dto.transferCard(test);
        });

        // then
        Assertions.assertEquals(ErrorCode.INVALID_VALUE, exception.getErrorCode());
    }
}
