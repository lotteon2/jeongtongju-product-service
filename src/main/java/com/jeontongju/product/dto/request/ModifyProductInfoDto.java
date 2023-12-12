package com.jeontongju.product.dto.request;

import javax.validation.constraints.Min;
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

  @Min(value = 1, message = "1원 이상만 입력 가능합니다.")
  private Long productPrice;

  @Min(value = 1, message = "1개 이상만 입력 가능합니다.")
  private Long registeredQuantity;

  private String productDetailsImageUrl;

  private Boolean isActivate;
}
