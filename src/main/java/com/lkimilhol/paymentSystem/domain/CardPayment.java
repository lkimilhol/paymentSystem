package com.lkimilhol.paymentSystem.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "CARD_DATA")
public class CardPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "UNIQUE_ID")
    long uniqueId;

    @Column(name = "CARD_DATA", columnDefinition = "CHAR(" + com.lkimilhol.paymentSystem.global.CardPayment.CARD_DATA_LEN + ")" , nullable = false)
    String cardData;

    @Column(name = "INSERT_TIME", columnDefinition = "DATETIME")
    private LocalDateTime insertTime;
}