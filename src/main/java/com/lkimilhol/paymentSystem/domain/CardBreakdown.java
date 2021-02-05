package com.lkimilhol.paymentSystem.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CardBreakdown {
    String uniqueId;

    private String cardNumber;

    private String expiryDate;

    private String cvc;

    private int installment;

    private int amount;

    private int vat;

    private boolean paymentStatus;
}
