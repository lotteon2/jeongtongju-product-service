package com.jeontongju.product.controller;

import com.jeontongju.product.dto.request.ProductDto;
import com.jeontongju.product.dto.response.CategoryDto;
import com.jeontongju.product.dto.response.MyProductInfoDto;
import com.jeontongju.product.dto.temp.ResponseFormat;
import com.jeontongju.product.enums.temp.MemberRoleEnum;
import com.jeontongju.product.service.ProductService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ProductRestController {

  private final ProductService productService;

  @GetMapping("/categories")
  public ResponseEntity<ResponseFormat<List<CategoryDto>>> getCategoryAll() {

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<List<CategoryDto>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("카테고리 종류 조회 성공")
                .data(productService.getCategoryAll())
                .build());
  }

  @PostMapping("/products")
  public ResponseEntity<ResponseFormat<Void>> createProduct(
      @Valid @RequestBody ProductDto product,
      @RequestHeader Long memberId,
      @RequestHeader MemberRoleEnum memberRole) {

    productService.createProduct(memberId, product);

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("상품 등록 성공")
                .build());
  }

  @GetMapping("/sellers/info/product")
  public ResponseEntity<ResponseFormat<List<MyProductInfoDto>>> getMyProductInfo(
      @RequestHeader Long memberId, @RequestHeader MemberRoleEnum memberRole) {

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<List<MyProductInfoDto>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("내 상품 정보 조회 성공")
                .data(productService.getMyProductInfo(memberId))
                .build());
  }
}
