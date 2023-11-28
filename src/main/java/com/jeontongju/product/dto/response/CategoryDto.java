package com.jeontongju.product.dto.response;

import com.jeontongju.product.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CategoryDto {

  private Long value;
  private String label;

  public static CategoryDto toDto(Category category) {

    return CategoryDto.builder().value(category.getCategoryId()).label(category.getName()).build();
  }
}
