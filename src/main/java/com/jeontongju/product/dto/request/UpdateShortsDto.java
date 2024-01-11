package com.jeontongju.product.dto.request;

import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UpdateShortsDto {

  private String shortsTitle;

  private String shortsDescription;

  private Boolean isActivate;
}
