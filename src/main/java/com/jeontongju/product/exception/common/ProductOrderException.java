package com.jeontongju.product.exception.common;

import org.springframework.http.HttpStatus;

public class ProductOrderException extends FeignException {

    public ProductOrderException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
