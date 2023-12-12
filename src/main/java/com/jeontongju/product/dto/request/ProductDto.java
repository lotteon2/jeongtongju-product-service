package com.jeontongju.product.dto.request;

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

  @Size(min = 1, max = 30, message = "내용은 1글자 이상 30 글자 이하만 입력 가능합니다.")
  @NotBlank(message = "null 이 불가합니다.")
  private String productName;

  @Size(min = 1, max = 30, message = "내용은 1글자 이상 30 글자 이하만 입력 가능합니다.")
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

  @Size(max = 2, min = 1, message = "최대 2개의 항목을 가져야 합니다.")
  private List<String> rawMaterial;

  private List<String> food;

  private List<String> concept;
}
