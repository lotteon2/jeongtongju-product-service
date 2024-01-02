package com.jeontongju.product.exception.common;

import io.github.bitbox.bitbox.enums.FailureTypeEnum;

public class CustomFailureFeignException extends RuntimeException {

  FailureTypeEnum failure;

  public CustomFailureFeignException(FailureTypeEnum failure) {
    super(failure.getValue());
    this.failure = failure;
  }

  public FailureTypeEnum getFailure() {
    return failure;
  }

}
