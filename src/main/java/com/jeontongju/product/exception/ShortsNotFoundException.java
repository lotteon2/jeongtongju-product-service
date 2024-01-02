package com.jeontongju.product.exception;

import com.jeontongju.product.exception.common.EntityNotFoundException;

public class ShortsNotFoundException extends EntityNotFoundException {
  private static final String message = "쇼츠를 찾을 수 없습니다";

  public ShortsNotFoundException() {
    super(message);
  }
}
