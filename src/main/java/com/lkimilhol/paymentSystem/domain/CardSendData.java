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
    @Column(name = "SEQ")
    private Long seq;

    @Lob
    @Column(name = "CARD_DATA", nullable = false, length = CardPaymentInfo.TOTAL_CARD_DATA_LEN)
    private String cardData;

    @Column(name = "SEND_TIME", columnDefinition = "DATETIME")
    private LocalDateTime sendTime;
}