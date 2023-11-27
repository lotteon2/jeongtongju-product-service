package com.jeontongju.product.dto.temp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 domain : all
 detail : Rest 통신시 사용되는 Success Format Dto
 method :
 comment :
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SuccessFormat {

  private final Integer code;
  private final String message;
  private final String detail;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private final Object data;
}
