package com.jeontongju.product.controller;

import com.jeontongju.product.dto.request.ModifyProductInfoDto;
import com.jeontongju.product.dto.request.ProductDto;
import com.jeontongju.product.dto.response.CategoryDto;
import com.jeontongju.product.dto.temp.ResponseFormat;
import com.jeontongju.product.enums.temp.MemberRoleEnum;
import com.jeontongju.product.exception.common.InvalidPermissionException;
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

  @DeleteMapping("/products/{productId}")
  public ResponseEntity<ResponseFormat<Void>> deleteProduct(
      @PathVariable String productId,
      @RequestHeader Long memberId,
      @RequestHeader MemberRoleEnum memberRole) {

    productService.deleteProduct(productId);

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("상품 삭제 성공")
                .build());
  }

  @PatchMapping("products/{productId}")
  public ResponseEntity<ResponseFormat<Void>> modifyProduct(
      @PathVariable String productId,
      @Valid @RequestBody ModifyProductInfoDto modifyProductInfoDto,
      @RequestHeader Long memberId,
      @RequestHeader MemberRoleEnum memberRole) {

    switch (memberRole) {
      case ROLE_ADMIN:
        productService.modifyProductByAdmin(productId, modifyProductInfoDto.getIsActivate());
        break;
      case ROLE_SELLER:
        productService.modifyProductBySeller(productId, modifyProductInfoDto);
        break;
      default:
        throw new InvalidPermissionException();
    }
    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("상품 수정 성공")
                .build());
  }
}
