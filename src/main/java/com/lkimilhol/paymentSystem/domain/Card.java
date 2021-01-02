package com.lkimilhol.paymentSystem.domain;

import lombok.Data;

@Data
public class Card {
    String cardNumber;
    String expiryDate;
    String csv;
    int installment;
    int amount;
    int vat;
}
