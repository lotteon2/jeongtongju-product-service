package com.jeontongju.product.exception;

import com.jeontongju.product.exception.common.EntityNotFoundException;

public class ProductRecodeNotFoundException extends EntityNotFoundException {

  private static final String message = "상품 이력 정보를 찾을 수 없습니다";

  public ProductRecodeNotFoundException() {
    super(message);
  }
}
