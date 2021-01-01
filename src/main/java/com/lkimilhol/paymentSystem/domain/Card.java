package com.lkimilhol.paymentSystem.domain;

import javax.persistence.*;

@Entity
@Table(name = "CARD")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CARD_ID")
    long id;

    @Column(name = "EXPIRY_DATE", nullable = false)
    String expiryDate;

    @Column(name = "CSV", nullable = false)
    String csv;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }
}
