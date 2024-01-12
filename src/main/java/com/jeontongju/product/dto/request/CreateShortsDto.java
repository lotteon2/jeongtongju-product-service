package com.jeontongju.product.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CreateShortsDto {

  @NotNull(message = "null 이 불가합니다.")
  private String shortsTitle;

  @NotNull(message = "null 이 불가합니다.")
  private String shortsDescription;

  @NotBlank(message = "null 이 불가합니다.")
  private String shortsPreviewUrl;

  @NotBlank(message = "null 이 불가합니다.")
  private String shortsVideoUrl;

  @NotNull(message = "null 이 불가합니다.")
  private String shortsThumbnailImageUrl;

  private String productId;

  @NotNull(message = "null 이 불가합니다.")
  private Boolean isActivate;
}
