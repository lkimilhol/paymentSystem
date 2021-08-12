package com.lkimilhol.paymentSystem.domain;

import com.lkimilhol.paymentSystem.global.CardPaymentInfo;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "CARD_ADMIN", indexes = @Index(name = "UNIQUE_ID_IDX", columnList = "UNIQUE_ID"))
public class CardAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SEQ")
    private Long seq;

    @Column(name = "UNIQUE_ID", length = CardPaymentInfo.COMMON_DATA_UNIQUE_ID_LEN)
    private String uniqueId;

    @Column(name = "PAYMENT_STATUS", columnDefinition="tinyint(1) default 1")
    private Boolean paymentStatus;

    @Lob
    @Column(name = "CARD_DATA", length = CardPaymentInfo.TOTAL_CARD_DATA_LEN)
    private String cardData;
}
