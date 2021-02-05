package com.lkimilhol.paymentSystem.global.common;

import com.lkimilhol.paymentSystem.global.CardPaymentInfo;

public class CommonUtility {
    public String appendStringSpace(String s, int numberOfSpace) {
        numberOfSpace = numberOfSpace - s.length();
        if(numberOfSpace == 0) {
            return s;
        }
        return String.format(s + "%" + numberOfSpace +"s", " ");
    }

    public String appendNumericSpace(int i, int numberOfSpace) {
        String s = Integer.toString(i);
        numberOfSpace = numberOfSpace - s.length();
        return String.format("%" + numberOfSpace +"s" + s, " ");
    }

    public String appendNumericZero(int i, int numberOfSpace) {
        return String.format("%0" + numberOfSpace + "d", i);
    }

    public String appendNumericNumberLeft(int i, int numberOfSpace) {
        String s = Integer.toString(i);
        numberOfSpace = numberOfSpace - s.length();
        if (numberOfSpace == 0) {return s;}
        return String.format(s + "%" + numberOfSpace +"s", " ");
    }

    public String appendNumericZero(Long l, int numberOfSpace) {
        return String.format("%0" + numberOfSpace + "d", l);
    }

    public String generateUniqueId(Long l) {
        long time = System.nanoTime();
        String id = appendNumericZero(l, CardPaymentInfo.GENERATE_UNIQUE_ID_ZERO_CNT);
        String timeString = Long.toString(time).substring(0, 10);
        return timeString + id;
    }

    public String setMask(String cardNum) {
        char[] ch = cardNum.toCharArray();
        for(int i = CardPaymentInfo.CARD_MASKING_START_LEN; i < cardNum.length() - CardPaymentInfo.CARD_MASKING_END_LEN; i ++){
            ch[i] = '*';
        }

        return String.valueOf(ch);
    }
}
