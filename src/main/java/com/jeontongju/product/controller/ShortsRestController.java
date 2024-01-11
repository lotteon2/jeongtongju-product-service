package com.jeontongju.product.controller;

import com.jeontongju.product.dto.request.CreateShortsDto;
import com.jeontongju.product.dto.request.UpdateShortsDto;
import com.jeontongju.product.dto.response.GetShortsByConsumerDto;
import com.jeontongju.product.dto.response.GetShortsBySellerDto;
import com.jeontongju.product.dto.response.GetShortsDetailsDto;
import com.jeontongju.product.service.ShortsService;
import io.github.bitbox.bitbox.dto.ResponseFormat;
import io.github.bitbox.bitbox.enums.MemberRoleEnum;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ShortsRestController {

  private final ShortsService shortsService;

  @GetMapping("/shorts")
  public ResponseEntity<ResponseFormat<Page<GetShortsByConsumerDto>>> getMainShorts(
      @PageableDefault(page = 0, sort = "shortsHits", direction = Sort.Direction.DESC, size = 6)
          Pageable pageable) {

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Page<GetShortsByConsumerDto>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("쇼츠 목록 조회 성공")
                .data(shortsService.getMainShorts(pageable))
                .build());
  }

  @GetMapping("/sellers/{sellerId}/shorts")
  public ResponseEntity<ResponseFormat<Page<GetShortsByConsumerDto>>> getOneSellerShorts(
      @PathVariable Long sellerId,
      @PageableDefault(page = 0, sort = "shortsHits", direction = Sort.Direction.DESC, size = 5)
          Pageable pageable) {

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Page<GetShortsByConsumerDto>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("셀러 소개 - 쇼츠 목록 조회 성공")
                .data(shortsService.getOneSellerShorts(sellerId, pageable))
                .build());
  }

  @GetMapping("/shorts/{shortsId}")
  public ResponseEntity<ResponseFormat<GetShortsDetailsDto>> getShortsDetails(
      @PathVariable Long shortsId) {

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<GetShortsDetailsDto>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("쇼츠 상세 조회 성공")
                .data(shortsService.getShortsDetails(shortsId))
                .build());
  }

  @GetMapping("/sellers/shorts")
  public ResponseEntity<ResponseFormat<Page<GetShortsBySellerDto>>> getShortsBySeller(
      @RequestHeader Long memberId,
      @RequestHeader MemberRoleEnum memberRole,
      @PageableDefault(page = 0, sort = "createdAt", direction = Sort.Direction.DESC, size = 10)
          Pageable pageable) {

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Page<GetShortsBySellerDto>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("쇼츠 목록 성공")
                .data(shortsService.getShortsBySeller(memberId, pageable))
                .build());
  }

  @PostMapping("/shorts")
  public ResponseEntity<ResponseFormat<Void>> createShorts(
      @RequestHeader Long memberId,
      @RequestHeader MemberRoleEnum memberRole,
      @Valid @RequestBody CreateShortsDto createShortsDto) {

    shortsService.createShorts(memberId, createShortsDto);

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("쇼츠 등록 성공")
                .build());
  }

  @PatchMapping("/sellers/shorts/{shortsId}")
  public ResponseEntity<ResponseFormat<Void>> updateShorts(
          @RequestHeader Long memberId,
          @RequestHeader MemberRoleEnum memberRole,
          @PathVariable Long shortsId,
          @Valid @RequestBody UpdateShortsDto updateShortsDto) {

    shortsService.updateShorts(shortsId, updateShortsDto);

    return ResponseEntity.ok()
            .body(
                    ResponseFormat.<Void>builder()
                            .code(HttpStatus.OK.value())
                            .message(HttpStatus.OK.name())
                            .detail("쇼츠 수정 성공")
                            .build());
  }

  @DeleteMapping("/shorts/{shortsId}")
  public ResponseEntity<ResponseFormat<Void>> deleteShorts(
          @RequestHeader Long memberId,
          @RequestHeader MemberRoleEnum memberRole,
          @PathVariable Long shortsId) {

    log.info("확인");
    if (memberId != null) {
      log.info("null 아님");
      log.info(String.valueOf(memberId));
      log.info(memberRole.toString());
    }


    shortsService.deleteShorts(shortsId);

    return ResponseEntity.ok()
            .body(
                    ResponseFormat.<Void>builder()
                            .code(HttpStatus.OK.value())
                            .message(HttpStatus.OK.name())
                            .detail("쇼츠 삭제 성공")
                            .build());
  }
}
