package com.jeontongju.product.exception;

import com.jeontongju.product.exception.common.EntityNotFoundException;

public class ProductNotFoundException  extends EntityNotFoundException {

    private static final String message = "상품을 찾을 수 없습니다";

    public ProductNotFoundException() {
        super(message);
    }
    
}