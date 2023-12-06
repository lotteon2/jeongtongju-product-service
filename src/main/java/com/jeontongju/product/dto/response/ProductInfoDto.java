package com.jeontongju.product.dto.response;

import com.jeontongju.product.domain.Product;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfoDto {

  private String value;
  private String label;

  public static ProductInfoDto toDto(Product product) {
    return ProductInfoDto.builder()
        .label(product.getProductId())
        .value(product.getName())
        .build();
  }
}
