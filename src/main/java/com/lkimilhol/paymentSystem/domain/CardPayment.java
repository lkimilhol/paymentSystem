package com.lkimilhol.paymentSystem.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "CARD_PAYMENT")
public class CardPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "UNIQUE_ID")
    private long uniqueId;

    @Column(name = "CARD_NUMBER")
    private long cardNumber;

    @Column(name = "EXPIRY_DATE")
    private int expiryDate;

    @Column(name = "CVC")
    private int cvc;

    @Column(name = "INSTALLMENT")
    private int installment;

    @Column(name = "AMOUNT")
    private int amount;

    @Column(name = "VAT")
    private int vat;

    @Column(name = "PAYMENT_STATUS", columnDefinition="tinyint(1) default 1")
    private boolean paymentStatus;

    @Column(name = "INSERT_TIME", columnDefinition = "DATETIME")
    private LocalDateTime insertTime;
}
