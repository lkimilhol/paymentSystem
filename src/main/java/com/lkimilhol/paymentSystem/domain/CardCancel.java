package com.lkimilhol.paymentSystem.domain;

import com.lkimilhol.paymentSystem.global.CardPaymentInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class CardCancel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SEQ")
    long seq;

    @Column(name = "UNIQUE_ID", nullable = false, length = CardPaymentInfo.COMMON_DATA_UNIQUE_ID_LEN, unique = true)
    String uniqueId;

    @Column(name = "SEND_TIME", columnDefinition = "DATETIME")
    private LocalDateTime sendTime;
}