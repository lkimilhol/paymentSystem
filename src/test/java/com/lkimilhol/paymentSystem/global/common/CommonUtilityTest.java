package com.lkimilhol.paymentSystem.global.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lkimilhol.paymentSystem.global.CardPaymentInfo;

class CommonUtilityTest {
    @Test
    @DisplayName("공백 채우기- 문자")
    public void appendSpace() {
        //given
        String s = "1234567890";

        //when
        String result = CommonUtility.fillSpace(s, CardPaymentInfo.CARD_NUMBER_LEN);

        //then
        Assertions.assertEquals(CardPaymentInfo.CARD_NUMBER_LEN, result.length());
        Assertions.assertEquals("1234567890          ", result);
    }

    @Test
    @DisplayName("공백 채우기- 숫자")
    public void appendIntegerSpace() {
        //given
        int i = 1234567890;

        //when
        String result = CommonUtility.fillSpace(i, CardPaymentInfo.CARD_NUMBER_LEN);

        //then
        Assertions.assertEquals(CardPaymentInfo.CARD_NUMBER_LEN, result.length());
        Assertions.assertEquals("          1234567890", result);
    }

    @Test
    @DisplayName("공백 채우기- 숫자0")
    public void appendIntegerZero() {
        //given
        int i = 1234567890;

        //when
        String result = CommonUtility.appendNumericZero(i, CardPaymentInfo.CARD_NUMBER_LEN);

        //then
        Assertions.assertEquals(CardPaymentInfo.CARD_NUMBER_LEN, result.length());
        Assertions.assertEquals("00000000001234567890", result);
    }

    @Test
    @DisplayName("공백 채우기- 숫자(좌측정렬)")
    public void appendIntegerSpaceRight() {
        //given
        int i = 1234567890;

        //when
        String result = CommonUtility.appendNumericNumberLeft(i, CardPaymentInfo.CARD_NUMBER_LEN);

        //then
        Assertions.assertEquals(CardPaymentInfo.CARD_NUMBER_LEN, result.length());
        Assertions.assertEquals("1234567890          ", result);
    }

    @Test
    @DisplayName("유니크 아이디 생성")
    public void generateUniqueId() {
        //given
        long l = 2100000000l;

        //when
        String s = CommonUtility.generateUniqueId(l);

        //then
        Assertions.assertEquals(CardPaymentInfo.COMMON_DATA_UNIQUE_ID_LEN, s.length());
    }

    @Test
    @DisplayName("마스킹 처리")
    public void setMask() {
        //given
        String cardNum = "1234567890123456";

        //when
        String s = CommonUtility.setMask(cardNum);

        //then
        Assertions.assertEquals("123456*******456", s);
    }

    @Test
    @DisplayName("카드 데이터 생성")
    public void makeCardData() {
    }
}