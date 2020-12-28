package com.lkimilhol.paymentSystem.domain;

import javax.persistence.*;

@Entity
@Table(name = "CARD")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CARD_ID")
    long cardId;

    @Column(name = "EXPIRY_DATE", nullable = false)
    int expiryDate;

    @Column(name = "CSV", nullable = false)
    int csv;

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public int getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(int expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getCsv() {
        return csv;
    }

    public void setCsv(int csv) {
        this.csv = csv;
    }
}
