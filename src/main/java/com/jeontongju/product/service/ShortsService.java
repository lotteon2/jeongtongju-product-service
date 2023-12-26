package com.jeontongju.product.service;

import com.jeontongju.product.dto.request.CreateShortsDto;
import com.jeontongju.product.dto.request.UpdateShortsDto;
import com.jeontongju.product.dto.response.GetShortsByConsumerDto;
import com.jeontongju.product.dto.response.GetShortsBySellerDto;
import com.jeontongju.product.dto.response.GetShortsDetailsDto;
import com.jeontongju.product.exception.ShortsNotFoundException;
import com.jeontongju.product.mapper.ProductMapper;
import com.jeontongju.product.repository.ShortsRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShortsService {

  private final ShortsRepository shortsRepository;
  private final ProductMapper productMapper;

  public List<GetShortsByConsumerDto> getMainShorts(Pageable pageable) {
    return shortsRepository.findShortsByIsDeletedAndIsActivate(false, true, pageable).stream()
        .map(shorts -> GetShortsByConsumerDto.toDto(shorts))
        .collect(Collectors.toList());
  }

  public List<GetShortsByConsumerDto> getOneSellerShorts(Long sellerId, Pageable pageable) {
    return shortsRepository
        .findShortsBySellerIdAndIsDeletedAndIsActivate(sellerId, false, true, pageable)
        .stream()
        .map(shorts -> GetShortsByConsumerDto.toDto(shorts))
        .collect(Collectors.toList());
  }

  public GetShortsDetailsDto getShortsDetails(Long shortsId) {

    return GetShortsDetailsDto.toDto(
        shortsRepository.findById(shortsId).orElseThrow(ShortsNotFoundException::new));
  }

  public List<GetShortsBySellerDto> getShortsBySeller(Long sellerId, Pageable pageable) {

    return shortsRepository.findShortsBySellerId(sellerId, pageable).stream()
        .map(shorts -> GetShortsBySellerDto.toDto(shorts))
        .collect(Collectors.toList());
  }

  @Transactional
  public void createShorts(Long memberId, CreateShortsDto createShortsDto) {
    shortsRepository.save(productMapper.toShortsEntity(memberId, createShortsDto));
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
