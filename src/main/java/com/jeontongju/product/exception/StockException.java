package com.jeontongju.product.exception;

import org.springframework.http.HttpStatus;

public class StockException extends RuntimeException {

    public StockException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }

}