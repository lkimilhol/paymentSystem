package com.lkimilhol.paymentSystem.global.common;

import com.lkimilhol.paymentSystem.global.CardPaymentInfo;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UtilityTest {
    private static CommonUtility commonUtility;

    @BeforeAll
    public static void setup() {
        commonUtility = new CommonUtility();
    }

    @Test
    @DisplayName("공백 채우기- 문자")
    public void appendSpace() {
        //given
        String s = "1234567890";
        CommonUtility commonUtility = new CommonUtility();

        //when
        String result = commonUtility.appendStringSpace(s, CardPaymentInfo.CARD_NUMBER_LEN);

        //then
        Assertions.assertEquals(CardPaymentInfo.CARD_NUMBER_LEN, result.length());
        Assertions.assertEquals("1234567890          ", result);
    }

    @Test
    @DisplayName("공백 채우기- 숫자")
    public void appendIntegerSpace() {
        //given
        int i = 1234567890;
        CommonUtility commonUtility = new CommonUtility();

        //when
        String result = commonUtility.appendNumericSpace(i, CardPaymentInfo.CARD_NUMBER_LEN);

        //then
        Assertions.assertEquals(CardPaymentInfo.CARD_NUMBER_LEN, result.length());
        Assertions.assertEquals("          1234567890", result);
    }

    @Test
    @DisplayName("공백 채우기- 숫자0")
    public void appendIntegerZero() {
        //given
        int i = 1234567890;
        CommonUtility commonUtility = new CommonUtility();

        //when
        String result = commonUtility.appendNumericZero(i, CardPaymentInfo.CARD_NUMBER_LEN);

        //then
        Assertions.assertEquals(CardPaymentInfo.CARD_NUMBER_LEN, result.length());
        Assertions.assertEquals("00000000001234567890", result);
    }

    @Test
    @DisplayName("공백 채우기- 숫자(좌측정렬)")
    public void appendIntegerSpaceRight() {
        //given
        int i = 1234567890;
        CommonUtility commonUtility = new CommonUtility();

        //when
        String result = commonUtility.appendNumericNumberLeft(i, CardPaymentInfo.CARD_NUMBER_LEN);

        //then
        Assertions.assertEquals(CardPaymentInfo.CARD_NUMBER_LEN, result.length());
        Assertions.assertEquals("1234567890          ", result);
    }

    @Test
    @DisplayName("유니크 아이디 생성")
    public void generateUniqueId() {
        long l = 2100000000l;
        String s = commonUtility.generateUniqueId(l);

        Assertions.assertEquals(CardPaymentInfo.COMMON_DATA_UNIQUE_ID_LEN, s.length());
    }

}
