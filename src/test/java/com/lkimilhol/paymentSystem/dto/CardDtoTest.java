package com.lkimilhol.paymentSystem.dto;

import com.lkimilhol.paymentSystem.domain.Card;
import com.lkimilhol.paymentSystem.global.error.CustomException;
import com.lkimilhol.paymentSystem.global.error.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CardDtoTest {

    @Test
    @DisplayName("정상 동작")
    void cardKeyCheck() {
        // given
        String test = "{\"cardNumber\":\"1234567890\", \"expiryDate\": \"0123\"," +
                "\"csv\":\"098\", \"installment\": 0," +
                "\"amount\":10000, \"vat\": 0}";

        // when
        CardDto dto = new CardDto();
        Card card = dto.transferCard(test);

        // then
        assertEquals(card.getCardNumber(), "1234567890");
        assertEquals(card.getExpiryDate(), "0123");
        assertEquals(card.getCsv(), "098");
        assertEquals(card.getInstallment(), 0);
        assertEquals(card.getAmount(), 10000);
        assertEquals(card.getVat(), 0);
    }


    @Test()
    @DisplayName("필수키 없음 에러")
    void cardRequiredKeyError() {
        // given
        String test = "{\"cardNumber\":\"1234567890\", \"expiryDate\": \"0123\"," +
                "\"csv\":\"098\", \"installment\": 0," +
                "vat\": 0}";

        // when
        CardDto dto = new CardDto();
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            Card card = dto.transferCard(test);
        });

        // then
        Assertions.assertEquals(ErrorCode.NOT_EXISTS_REQUIRED_KEY, exception.getErrorCode());
    }
}
