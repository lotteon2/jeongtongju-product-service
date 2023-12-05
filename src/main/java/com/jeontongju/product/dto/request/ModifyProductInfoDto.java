package com.jeontongju.product.dto.request;

import javax.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class ModifyProductInfoDto {

  @Size(min = 1, max = 30, message = "내용은 1글자 이상 30 글자 이하만 입력 가능합니다.")
  private String productName;

  private String productThumbnailImageUrl;

  private Long productPrice;

  private Long registeredQuantity;

  private String productDetailsImageUrl;

  private Boolean isActivate;
}
