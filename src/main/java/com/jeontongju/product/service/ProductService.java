package com.jeontongju.product.service;

import com.jeontongju.product.dto.response.CategoryDto;
import com.jeontongju.product.repository.CategoryRepository;
import com.jeontongju.product.repository.ProductDetailsImageRepository;
import com.jeontongju.product.repository.ProductRepository;
import com.jeontongju.product.repository.ProductThumbnailImageRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

  private final CategoryRepository categoryRepository;
  private final ProductRepository productRepository;
  private final ProductThumbnailImageRepository productThumbnailImageRepository;
  private final ProductDetailsImageRepository productDetailsImageRepository;

  public List<CategoryDto> getCategoryAll() {
    return categoryRepository.findAll().stream()
        .map(CategoryDto::toDto)
        .collect(Collectors.toList());
  }
}
