package com.lkimilhol.paymentSystem.responseApi;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class CardGetResponse {
    HttpStatus status;
    String uniqueId;
    String cardNum;
    String expiryDate;
    String cvc;
    int isPayment;
    int amount;
    int vat;
}
