package com.lkimilhol.paymentSystem.global.common;

import com.lkimilhol.paymentSystem.domain.CardPayment;
import com.lkimilhol.paymentSystem.global.CardPaymentInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AES256UtilityTest {
    @Test
    @DisplayName("AES 암호화 / 복호화")
    public void encryptAes(){
        //given
        AES256Utility util = new AES256Utility();
        CardPayment cardPayment = new CardPayment();
        String cardNumber = "1234567890";
        int installment = 0;
        String csv = "098";
        cardPayment.setCardNumber(cardNumber);
        cardPayment.setInstallment(installment);
        cardPayment.setCsv(csv);

        //when
        String encryptCardInfo = util.encryptCardInfo(cardPayment);
        String original = util.decryptCardInfo(encryptCardInfo);
        String[] result = original.split(CardPaymentInfo.CARD_INFORMATION_DELIMITER);

        //then
        assertAll("result",
                () -> assertEquals(cardNumber, result[0]),
                () -> assertEquals(installment, Integer.parseInt(result[1])),
                () -> assertEquals(csv, result[2]));
    }
}
