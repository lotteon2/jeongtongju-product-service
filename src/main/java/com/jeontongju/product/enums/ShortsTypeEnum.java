package com.jeontongju.product.enums;

import lombok.Getter;

@Getter
public enum ShortsTypeEnum {
  PRODUCT("상품"),
  SELLER("판매 숍");

  private final String value;

  ShortsTypeEnum(String value) {
    this.value = value;
  }
}
