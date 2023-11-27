package com.jeontongju.product.entity;

import lombok.Getter;

@Getter
public enum ShortsType {
  PRODUCT("상품"),
  SELLER("판매 숍");

  private final String value;

  ShortsType(String value) {
    this.value = value;
  }
}
