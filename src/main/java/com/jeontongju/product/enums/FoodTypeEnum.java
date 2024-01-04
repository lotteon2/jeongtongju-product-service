package com.jeontongju.product.enums;

import lombok.Getter;

@Getter
public enum FoodTypeEnum {
  PIZZA("🍕 피자"),
  KOREAN_PIZZA("🫓 전"),
  CHEESE("🧀 치즈"),
  FRUIT("🍉 과일"),
  CHOCOLATE("🍫 초콜릿"),
  FINGER_FOOD("🌮 주전부리"),
  BEEF("🐂 소고기"),
  LAMB("🐏 양고기"),
  PIG_HOCKS("🐷 족발/보쌈"),
  BARBECUE("🍖 바베큐"),
  BOILED_CHICKEN("🐓 백숙"),
  CHICKEN("🍗 치킨 "),
  RAW_FISH("🍣 생선 회"),
  GRILLED_SEAFOOD("🦀 해산물 구이"),
  WHITE_SOUP("🍲 백탕"),
  RED_SOUP("🥘 홍탕");

  private final String value;

  FoodTypeEnum(String value) {
    this.value = value;
  }
}
