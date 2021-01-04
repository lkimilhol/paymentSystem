package com.lkimilhol.paymentSystem.controller;

import com.lkimilhol.paymentSystem.global.error.CustomException;
import com.lkimilhol.paymentSystem.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.lkimilhol.paymentSystem.global.error.ErrorCode.SERVER_ERROR;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(CustomException.class)
    public @ResponseBody
    ResponseEntity<ErrorResponse> error(CustomException e){
        final ErrorResponse res = new ErrorResponse();
        res.setErrorCode(e.getErrorCode());
        res.setErrorNumber(e.getErrorCode().getCode());
        res.setMessage(e.getErrorCode().getMessage());
        return new ResponseEntity<>(res, res.getErrorCode().getStatus());
    }

    @ExceptionHandler(value = Exception.class)
    public @ResponseBody
    ResponseEntity<ErrorResponse> error(Exception e) {
        final ErrorResponse res = new ErrorResponse();
        res.setErrorCode(SERVER_ERROR);
        res.setErrorNumber(SERVER_ERROR.getCode());
        res.setMessage(SERVER_ERROR.getMessage());
        return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
