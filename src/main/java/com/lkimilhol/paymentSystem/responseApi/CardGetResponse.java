package com.lkimilhol.paymentSystem.responseApi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardGetResponse {
    String uniqueId;
    String cardNum;
    String expiryDate;
    String cvc;
    int isPayment;
    int amount;
    int vat;
}
