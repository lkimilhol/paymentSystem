package com.lkimilhol.paymentSystem.responseApi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardCancelResponse {
    String uniqueId;
    String cancelData;
}
