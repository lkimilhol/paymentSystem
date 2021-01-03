package com.lkimilhol.paymentSystem.global;

public class Utility {
    public String AppendStringSpace(String s, int numberOfSpace) {
        numberOfSpace = numberOfSpace - s.length();
        return String.format(s + "%" + numberOfSpace +"s", " ");
    }

    public String AppendIntegerSpace(int i, int numberOfSpace) {
        String s = Integer.toString(i);
        numberOfSpace = numberOfSpace - s.length();
        return String.format("%" + numberOfSpace +"s" + s, " ");
    }

    public String AppendIntegerZero(int i, int numberOfSpace) {
        return String.format("%0" + numberOfSpace + "d", i);
    }

    public String AppendIntegerSpaceRight(int i, int numberOfSpace) {
        String s = Integer.toString(i);
        numberOfSpace = numberOfSpace - s.length();
        return String.format(s + "%" + numberOfSpace +"s", " ");
    }

}
