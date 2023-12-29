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

  @Size(min = 1, max = 30, message = "내용은 1글자 이상 30 글자 이하만 입력 가능합니다.")
  @NotNull(message = "null 이 불가합니다.")
  private String shortsTitle;

  @Size(min = 1, max = 300, message = "내용은 1글자 이상 300 글자 이하만 입력 가능합니다.")
  @NotNull(message = "null 이 불가합니다.")
  private String shortsDescription;

  @NotNull(message = "null 이 불가합니다.")
  private String shortsPreviewUrl;

  @NotNull(message = "null 이 불가합니다.")
  private String shortsVideoUrl;

  @NotNull(message = "null 이 불가합니다.")
  private String shortsThumbnailImageUrl;

  private String productId;

  @NotNull(message = "null 이 불가합니다.")
  private Boolean isActivate;
}
