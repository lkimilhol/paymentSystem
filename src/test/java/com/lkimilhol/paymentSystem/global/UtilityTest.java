package com.lkimilhol.paymentSystem.global;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UtilityTest {
    @Test
    @DisplayName("공백 채우기- 문자")
    public void AppendSpace() {
        //given
        String s = "1234567890";
        Utility utility = new Utility();

        //when
        String result = utility.AppendStringSpace(s, CardPaymentInfo.CARD_NUMBER_LEN);

        //then
        Assertions.assertEquals(CardPaymentInfo.CARD_NUMBER_LEN, result.length());
        Assertions.assertEquals("1234567890          ", result);
    }

    @Test
    @DisplayName("공백 채우기- 숫자")
    public void AppendIntegerSpace() {
        //given
        int i = 1234567890;
        Utility utility = new Utility();

        //when
        String result = utility.AppendIntegerSpace(i, CardPaymentInfo.CARD_NUMBER_LEN);

        //then
        Assertions.assertEquals(CardPaymentInfo.CARD_NUMBER_LEN, result.length());
        Assertions.assertEquals("          1234567890", result);
    }

    @Test
    @DisplayName("공백 채우기- 숫자0")
    public void AppendIntegerZero() {
        //given
        int i = 1234567890;
        Utility utility = new Utility();

        //when
        String result = utility.AppendIntegerZero(i, CardPaymentInfo.CARD_NUMBER_LEN);

        //then
        Assertions.assertEquals(CardPaymentInfo.CARD_NUMBER_LEN, result.length());
        Assertions.assertEquals("00000000001234567890", result);
    }

    @Test
    @DisplayName("공백 채우기- 숫자(좌측정렬)")
    public void AppendIntegerSpaceRight() {
        //given
        int i = 1234567890;
        Utility utility = new Utility();

        //when
        String result = utility.AppendIntegerSpaceRight(i, CardPaymentInfo.CARD_NUMBER_LEN);

        //then
        Assertions.assertEquals(CardPaymentInfo.CARD_NUMBER_LEN, result.length());
        Assertions.assertEquals("1234567890          ", result);
    }

}
