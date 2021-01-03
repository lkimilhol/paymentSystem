package com.lkimilhol.paymentSystem.global.error;

import lombok.Getter;
import lombok.Setter;

import java.net.http.HttpResponse;

@Getter
@Setter
public class ErrorResponse  {
    ErrorCode errorCode;
}
