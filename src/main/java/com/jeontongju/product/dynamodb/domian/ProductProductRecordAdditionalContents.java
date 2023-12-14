package com.jeontongju.product.dynamodb.domian;

import com.jeontongju.product.dto.request.ProductDto;
import com.jeontongju.product.vo.Taste;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductProductRecordAdditionalContents {

  private String productId;

  private Taste taste;

  private List<String> rawMaterial;

  private List<String> food;

  private List<String> concept;

  public static ProductProductRecordAdditionalContents toDto(
      String productId, ProductDto productRequestDto) {

    return ProductProductRecordAdditionalContents.builder()
        .productId(productId)
        .taste(productRequestDto.getTaste())
        .rawMaterial(productRequestDto.getRawMaterial())
        .concept(productRequestDto.getConcept())
        .food(productRequestDto.getFood())
        .build();
  }
}
