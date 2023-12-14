package com.jeontongju.product.exception.common;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends DomainException {

  public EntityNotFoundException(String message) {
    super(message);
  }

  @Override
  public HttpStatus getStatus() {
    return HttpStatus.NOT_FOUND;
  }
}
