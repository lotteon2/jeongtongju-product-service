package com.jeontongju.product.controller;

import com.jeontongju.product.dto.response.CategoryDto;
import com.jeontongju.product.dto.temp.SuccessFormat;
import com.jeontongju.product.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ProductRestController {

  private final ProductService productService;

  @GetMapping("/categories")
  public ResponseEntity<SuccessFormat<List<CategoryDto>>> getCategoryAll() {

    return ResponseEntity.ok()
        .body(
            SuccessFormat.<List<CategoryDto>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("카테고리 종류 조회 성공")
                .data(productService.getCategoryAll())
                .build());
  }
}
