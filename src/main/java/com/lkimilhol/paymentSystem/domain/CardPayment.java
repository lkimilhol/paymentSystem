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
    long uniqueId;

    @Column(name = "CARD_NUMBER")
    long cardNumber;

    @Column(name = "EXPIRY_DATE")
    int expiryDate;

    @Column(name = "CSV")
    int csv;

    @Column(name = "INSTALLMENT")
    int installment;

    @Column(name = "AMOUNT")
    int amount;

    @Column(name = "VAT")
    int vat;

    @Column(name = "INSERT_TIME", columnDefinition = "DATETIME")
    private LocalDateTime insertTime;
}
