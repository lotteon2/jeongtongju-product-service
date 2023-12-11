package com.jeontongju.product.dto.request;

import java.util.List;

import com.jeontongju.product.vo.Taste;
import lombok.*;

import javax.validation.constraints.*;

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

  @DecimalMax(value = "9.9", message = "소수점 첫째 자리까지만 입력 가능합니다.")
  @NotNull(message = "null 이 불가합니다.")
  private Double productAlcoholDegree;

  @NotNull(message = "null 이 불가합니다.")
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
  private Long productPrice;

  @NotNull(message = "null 이 불가합니다.")
  private Long registeredQuantity;

  private String productDetailsImageUrl;

  @NotNull(message = "null 이 불가합니다.")
  private Long categoryId;

  @NotNull(message = "null 이 불가합니다.")
  private Taste taste;

  private List<String> rawMaterial;

  private List<String> food;

  private List<String> concept;

}
