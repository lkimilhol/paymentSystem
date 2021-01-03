package com.lkimilhol.paymentSystem.global.common;

public class CommonUtility {
    public String AppendStringSpace(String s, int numberOfSpace) {
        numberOfSpace = numberOfSpace - s.length();
        return String.format(s + "%" + numberOfSpace +"s", " ");
    }

    public String AppendNumericSpace(int i, int numberOfSpace) {
        String s = Integer.toString(i);
        numberOfSpace = numberOfSpace - s.length();
        return String.format("%" + numberOfSpace +"s" + s, " ");
    }

    public String AppendNumericZero(int i, int numberOfSpace) {
        return String.format("%0" + numberOfSpace + "d", i);
    }

    public String AppendNumericNumberLeft(int i, int numberOfSpace) {
        String s = Integer.toString(i);
        if (s.length() < numberOfSpace) {
            s = "0" + s;
        }
        numberOfSpace = numberOfSpace - s.length();
        if (numberOfSpace == 0) {return s;}
        return String.format(s + "%" + numberOfSpace +"s", " ");
    }

    public String AppendNumericNumberLeft(Long l, int numberOfSpace) {
        String s = Long.toString(l);
        numberOfSpace = numberOfSpace - s.length();
        return String.format(s + "%" + numberOfSpace +"s", " ");
    }
}
