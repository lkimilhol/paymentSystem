package com.lkimilhol.paymentSystem.domain;

import com.lkimilhol.paymentSystem.global.CardPaymentInfo;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "CARD_SEND_DATA")
public class CardSendData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "UNIQUE_ID")
    long uniqueId;

    @Column(name = "CARD_DATA", columnDefinition = "CHAR(" + CardPaymentInfo.CARD_DATA_LEN + ")", nullable = false )
    String cardData;

    @Column(name = "SEND_TIME", columnDefinition = "DATETIME")
    private LocalDateTime sendTime;
}