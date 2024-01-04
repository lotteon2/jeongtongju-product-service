package com.jeontongju.product.enums;

import lombok.Getter;

@Getter
public enum ConceptTypeEnum {
  CAMPING("⛺ 캠핑 "),
  FISHING("\uD83C\uDFA3 낚시"),
  PARTY("\uD83E\uDEA9 파티"),
  GATHERING("\uD83D\uDC65 모임"),
  YEAR_END("\uD83C\uDF89연말"),
  HIKING("🧗‍♀️등산"),
  OUTING("\uD83E\uDDFA 나들이"),
  TRIP("\uD83D\uDE97 여행"),
  GIFT("\uD83C\uDF81 선물"),
  MEETING_THE_FAMILY ("👨‍👩‍👧‍👧상견례"),
  HOLIDAY("🎎명절"),
  ROOPTOP("\uD83C\uDF8E 루프탑"),
  HEALING("\uD83C\uDFE5 힐링"),
  EMOTION ("❤️감성"),
  HANGOVER_REMEDY("\uD83E\uDD2E 숙취퇴치");

  private final String value;

  ConceptTypeEnum(String value) {
    this.value = value;
  }
}
