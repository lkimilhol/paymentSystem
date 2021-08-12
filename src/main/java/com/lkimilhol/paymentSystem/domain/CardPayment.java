package com.lkimilhol.paymentSystem.domain;

import com.lkimilhol.paymentSystem.global.CardPaymentInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "CARD_PAYMENT", indexes = @Index(name = "PAYMENT_UNIQUE_ID_IDX", columnList = "UNIQUE_ID"))
public class CardPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PAYMENT_SEQ")
    private long seq;

    @Column(name = "UNIQUE_ID", nullable = false, length = CardPaymentInfo.COMMON_DATA_UNIQUE_ID_LEN, unique = true)
    private String uniqueId;

    @Column(name = "CARD_NUMBER", nullable = false, length = CardPaymentInfo.CARD_NUMBER_LEN)
    private String cardNumber;

    @Column(name = "EXPIRY_DATE", nullable = false, length = CardPaymentInfo.CARD_EXPIRY_DATE_LEN)
    private String expiryDate;

    @Column(name = "CVC", nullable = false, length = CardPaymentInfo.CARD_CVC_LEN)
    private String cvc;

    @Column(name = "INSTALLMENT")
    private int installment;

    @Column(name = "AMOUNT")
    private int amount;

    @Column(name = "VAT")
    private int vat;

    @Column(name = "PAYMENT_STATUS", columnDefinition = "tinyint(1) default 1")
    private Boolean paymentStatus;

    @Column(name = "INSERT_TIME", columnDefinition = "DATETIME")
    private LocalDateTime insertTime;

    @OneToMany(mappedBy = "cardPayment")
    private List<CardCancel> cardCancels = new ArrayList<>();
}
