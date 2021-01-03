package com.lkimilhol.paymentSystem.domain;

import com.lkimilhol.paymentSystem.global.CardPaymentInfo;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "CARD_SEND_DATA", indexes = @Index(name = "UNIQUE_ID_IDX", columnList = "UNIQUE_ID"))
public class CardSendData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SEQ")
    long seq;

    @Column(name = "UNIQUE_ID", nullable = false, length = CardPaymentInfo.COMMON_DATA_UNIQUE_ID_LEN, unique = true)
    String uniqueId;

    @Lob
    @Column(name = "CARD_DATA", nullable = false, length = CardPaymentInfo.TOTAL_CARD_DATA_LEN)
    String cardData;

    @Column(name = "SEND_TIME", columnDefinition = "DATETIME")
    private LocalDateTime sendTime;
}