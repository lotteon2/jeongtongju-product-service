package com.jeontongju.product.controller;

import com.jeontongju.product.service.ProductService;
import io.github.bitbox.bitbox.dto.FeignFormat;
import io.github.bitbox.bitbox.dto.ProductInfoDto;
import io.github.bitbox.bitbox.dto.ProductSearchDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductClientController {

  private final ProductService productService;

  @PostMapping("/products")
  ResponseEntity<FeignFormat<List<ProductInfoDto>>> getProductInfoForOrder(
      @RequestBody ProductSearchDto productSearchDto) {

    return ResponseEntity.ok(
        FeignFormat.<List<ProductInfoDto>>builder()
            .code(HttpStatus.OK.value())
            .data(productService.getProductInfoForOrder(productSearchDto))
            .build());
  }

  @GetMapping("/products/{productId}/image")
  ResponseEntity<FeignFormat<String>> getProductImage(@PathVariable String productId) {

    return ResponseEntity.ok(
        FeignFormat.<String>builder()
            .code(HttpStatus.OK.value())
            .data(productService.getProductImage(productId))
            .build());
  }
}
