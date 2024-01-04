package com.jeontongju.product.dynamodb.domian;

import com.jeontongju.product.dto.request.ProductDto;
import com.jeontongju.product.enums.ConceptTypeEnum;
import com.jeontongju.product.enums.FoodTypeEnum;
import com.jeontongju.product.enums.RawMeterialTypeEnum;
import com.jeontongju.product.vo.Taste;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRecordAdditionalContents {

  private String productId;

  private Taste taste;

  private List<String> rawMaterial;

  private List<String> food;

  private List<String> concept;

  public static ProductRecordAdditionalContents toDto(
      String productId, ProductDto productRequestDto) {

    return ProductRecordAdditionalContents.builder()
        .productId(productId)
        .taste(productRequestDto.getTaste())
        .rawMaterial(
            productRequestDto.getRawMaterial().stream()
                .map(RawMeterialTypeEnum::getValue)
                .collect(Collectors.toList()))
        .concept(
            productRequestDto.getConcept().stream()
                .map(ConceptTypeEnum::getValue)
                .collect(Collectors.toList()))
        .food(
            productRequestDto.getFood().stream()
                .map(FoodTypeEnum::getValue)
                .collect(Collectors.toList()))
        .build();
  }
}
