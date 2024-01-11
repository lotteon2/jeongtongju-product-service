package com.jeontongju.product.service;

import com.jeontongju.product.domain.Product;
import com.jeontongju.product.domain.Shorts;
import com.jeontongju.product.dto.request.CreateShortsDto;
import com.jeontongju.product.dto.request.UpdateShortsDto;
import com.jeontongju.product.dto.response.GetShortsByConsumerDto;
import com.jeontongju.product.dto.response.GetShortsBySellerDto;
import com.jeontongju.product.dto.response.GetShortsDetailsDto;
import com.jeontongju.product.dynamodb.domian.ProductRecord;
import com.jeontongju.product.dynamodb.domian.ProductRecordContents;
import com.jeontongju.product.dynamodb.domian.ProductRecordId;
import com.jeontongju.product.dynamodb.repository.ProductMetricsRepository;
import com.jeontongju.product.dynamodb.repository.ProductRecordRepository;
import com.jeontongju.product.exception.ProductNotFoundException;
import com.jeontongju.product.exception.ShortsNotFoundException;
import com.jeontongju.product.mapper.ProductMapper;
import com.jeontongju.product.repository.ProductRepository;
import com.jeontongju.product.repository.ShortsRepository;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShortsService {

  private final ShortsRepository shortsRepository;
  private final ProductRepository productRepository;
  private final ProductRecordRepository productRecordRepository;
  private final ProductMapper productMapper;

  public Page<GetShortsByConsumerDto> getMainShorts(Pageable pageable) {

    Page<Shorts> shortsList =
        shortsRepository.findShortsByIsDeletedAndIsActivate(false, true, pageable);

    return new PageImpl<GetShortsByConsumerDto>(
        shortsList.getContent().stream()
            .map(shorts -> GetShortsByConsumerDto.toDto(shorts))
            .collect(Collectors.toList()),
        pageable,
        shortsList.getTotalElements());
  }

  public Page<GetShortsByConsumerDto> getOneSellerShorts(Long sellerId, Pageable pageable) {
    Page<Shorts> shortsList =
        shortsRepository.findShortsBySellerIdAndIsDeletedAndIsActivate(
            sellerId, false, true, pageable);

    return new PageImpl<GetShortsByConsumerDto>(
        shortsList.getContent().stream()
            .map(shorts -> GetShortsByConsumerDto.toDto(shorts))
            .collect(Collectors.toList()),
        pageable,
        shortsList.getTotalElements());
  }

  public GetShortsDetailsDto getShortsDetails(Long shortsId) {

    return GetShortsDetailsDto.toDto(
        shortsRepository.findById(shortsId).orElseThrow(ShortsNotFoundException::new));
  }

  public Page<GetShortsBySellerDto> getShortsBySeller(Long sellerId, Pageable pageable) {

    Page<Shorts> shortsList = shortsRepository.findShortsBySellerIdAndIsDeleted(sellerId, pageable, false);
    return new PageImpl<GetShortsBySellerDto>(
        shortsList.getContent().stream()
            .map(shorts -> GetShortsBySellerDto.toDto(shorts))
            .collect(Collectors.toList()),
        pageable,
        shortsList.getTotalElements());
  }

  @Transactional
  public void createShorts(Long memberId, CreateShortsDto createShortsDto) {
    Shorts shorts = shortsRepository.save(productMapper.toShortsEntity(memberId, createShortsDto));
    String productId = createShortsDto.getProductId();
    if (productId != null) {
      Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
      // 변경 이력 - dynamo db
      ProductRecordContents updateProductRecord =
              ProductRecordContents.toDto(createShortsDto.getProductId(), product, shorts.getShortsId());
      productRecordRepository.save(
              ProductRecord.builder()
                      .productRecordId(
                              ProductRecordId.builder()
                                      .productId(product.getProductId())
                                      .createdAt(LocalDateTime.now().toString())
                                      .build())
                      .productRecord(updateProductRecord)
                      .action("UPDATE-SHORTS")
                      .build());
    }
  }

  @Transactional
  public void updateShorts(Long shortsId, UpdateShortsDto updateShortsDto) {

    shortsRepository
        .findById(shortsId)
        .orElseThrow(ShortsNotFoundException::new)
        .modifyShorts(updateShortsDto);
  }

  @Transactional
  public void deleteShorts(Long shortsId) {
    shortsRepository.findById(shortsId).orElseThrow(ShortsNotFoundException::new).setDeleted(true);
  }
}
