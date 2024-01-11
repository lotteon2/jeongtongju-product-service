package com.jeontongju.product.dto.request;

import com.jeontongju.product.enums.ConceptTypeEnum;
import com.jeontongju.product.enums.FoodTypeEnum;
import com.jeontongju.product.enums.RawMeterialTypeEnum;
import com.jeontongju.product.vo.Taste;
import java.util.List;
import javax.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class ProductDto {

  @NotBlank(message = "null 이 불가합니다.")
  private String productName;

  @NotBlank(message = "null 이 불가합니다.")
  private String productDescription;

  private String productThumbnailImageUrl;

  @NotNull(message = "null 이 불가합니다.")
  @DecimalMin(value = "0.0", message = "0 이상만 입력 가능합니다.")
  private Double productAlcoholDegree;

  @NotNull(message = "null 이 불가합니다.")
  @Min(value = 1, message = "1 이상만 입력 가능합니다.")
  private Long productCapacity;

  @NotBlank(message = "내용은 null 이 불가합니다.")
  private String breweryName;

  @NotBlank(message = "내용은 null 이 불가합니다.")
  private String breweryZonecode;

  @NotBlank(message = "내용은 null 이 불가합니다.")
  private String breweryAddress;

  private String breweryAddressDetails;

  @NotBlank(message = "내용은 null 이 불가합니다.")
  private String manufacturer;

  @NotNull(message = "null 이 불가합니다.")
  @Min(value = 1, message = "1원 이상만 입력 가능합니다.")
  private Long productPrice;

  @NotNull(message = "null 이 불가합니다.")
  @Min(value = 1, message = "1개 이상만 입력 가능합니다.")
  private Long registeredQuantity;

  private String productDetailsImageUrl;

  @NotNull(message = "null 이 불가합니다.")
  private Long categoryId;

  @NotNull(message = "null 이 불가합니다.")
  private Taste taste;

  @Size(max = 2, min = 1, message = "1개 또는 2개 항목을 가져야 합니다.")
  private List<RawMeterialTypeEnum> rawMaterial;

  private List<FoodTypeEnum> food;

  private List<ConceptTypeEnum> concept;
}
