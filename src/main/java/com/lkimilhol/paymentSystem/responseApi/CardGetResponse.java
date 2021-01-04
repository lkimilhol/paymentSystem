package com.lkimilhol.paymentSystem.responseApi;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CardGetResponse {
    HttpStatus status = HttpStatus.OK;
    String uniqueId;
    String cardNum;
    String expiryDate;
    String cvc;
    int isPayment;
    int amount;
    int vat;
}
