package com.lkimilhol.paymentSystem.controller;

import com.lkimilhol.paymentSystem.global.error.CustomException;
import com.lkimilhol.paymentSystem.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(CustomException.class)
    public @ResponseBody
    ResponseEntity<ErrorResponse> error(CustomException e){
        final ErrorResponse res = new ErrorResponse();
        res.setErrorCode(e.getErrorCode());
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }
}
