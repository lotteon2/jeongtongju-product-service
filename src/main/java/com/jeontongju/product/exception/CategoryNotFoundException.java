package com.jeontongju.product.exception;

import com.jeontongju.product.exception.common.EntityNotFoundException;

public class CategoryNotFoundException extends EntityNotFoundException {
  private static final String message = "카테고리를 찾을 수 없습니다";

  public CategoryNotFoundException() {
    super(message);
  }
}
