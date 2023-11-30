package com.jeontongju.product.exception.common;


public class FeignServerException extends RuntimeException {

    public FeignServerException(String message) {
        super(message);
    }
}
