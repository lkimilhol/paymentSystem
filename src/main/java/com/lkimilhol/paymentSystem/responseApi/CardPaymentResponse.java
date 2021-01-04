package com.lkimilhol.paymentSystem.responseApi;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CardPaymentResponse {
    HttpStatus status = HttpStatus.OK;
    String uniqueId;
    String cardData;
}
