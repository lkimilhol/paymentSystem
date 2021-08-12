package com.lkimilhol.paymentSystem.domain;

import com.lkimilhol.paymentSystem.global.CardPaymentInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "CARD_CANCEL", indexes = @Index(name = "CANCEL_UNIQUE_ID_IDX", columnList = "UNIQUE_ID"))
public class CardCancel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CANCEL_SEQ")
    private Long seq;

    @Column(name = "UNIQUE_ID", nullable = false, length = CardPaymentInfo.COMMON_DATA_UNIQUE_ID_LEN, unique = true)
    String uniqueId;

    @Column(name = "AMOUNT")
    private int amount;

    @Column(name = "VAT")
    private int vat;

    @ManyToOne
    @JoinColumn(name = "PAYMENT_SEQ")
    private CardPayment cardPayment;

    @Column(name = "INSERT_TIME", columnDefinition = "DATETIME")
    private LocalDateTime insertTime;

    private Boolean isPartCancel;
}