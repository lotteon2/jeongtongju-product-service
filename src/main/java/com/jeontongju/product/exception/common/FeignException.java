package com.jeontongju.product.exception.common;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class FeignException extends RuntimeException {
    private final Map<String, String> validation = new HashMap<>();

    public FeignException(String message) {
        super(message);
    }

    public abstract HttpStatus getStatus();

    public void addValidation(String fieldName, String errorMessage) {
        validation.put(fieldName, errorMessage);
    }
}