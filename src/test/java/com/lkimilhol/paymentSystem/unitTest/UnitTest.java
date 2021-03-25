package com.lkimilhol.paymentSystem.unitTest;

import com.lkimilhol.paymentSystem.domain.CardPayment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UnitTest {
    @Test
    @DisplayName("CardPayment unique id 설정 확인")
    public void testCardPaymentUniqueId() {
        CardPayment cardPayment = new CardPayment();
        cardPayment.setUniqueId("test");
        setCardPaymentUniqueId(cardPayment);

        assertEquals("change", cardPayment.getUniqueId());
    }

    private void setCardPaymentUniqueId(CardPayment cardPayment) {
        cardPayment.setUniqueId("change");
    }
}
