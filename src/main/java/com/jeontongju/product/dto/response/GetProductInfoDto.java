package com.jeontongju.product.dto.response;

import com.jeontongju.product.domain.Product;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetProductInfoDto {

  private String value;
  private String label;

  public static GetProductInfoDto toDto(Product product) {
    return GetProductInfoDto.builder()
        .label(product.getProductId())
        .value(product.getName())
        .build();
  }
}
