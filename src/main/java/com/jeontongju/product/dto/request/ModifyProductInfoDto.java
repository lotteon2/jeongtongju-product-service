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

  private String productName;

  private String productThumbnailImageUrl;

  @Min(value = 1, message = "1원 이상만 입력 가능합니다.")
  private Long productPrice;

  @Min(value = 1, message = "1개 이상만 입력 가능합니다.")
  private Long registeredQuantity;

  private String productDetailsImageUrl;

  private Boolean isActivate;
}
