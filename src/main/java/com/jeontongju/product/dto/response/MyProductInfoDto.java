package com.jeontongju.product.dto.response;

import com.jeontongju.product.domain.Product;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyProductInfoDto {

  private String value;
  private String label;

  public static MyProductInfoDto toDto(Product product) {
    return MyProductInfoDto.builder()
        .label(product.getProductId())
        .value(product.getName())
        .build();
  }
}
