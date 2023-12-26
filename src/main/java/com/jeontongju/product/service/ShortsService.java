package com.jeontongju.product.service;

import com.jeontongju.product.dto.response.GetShortsByConsumerDto;
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

  public List<GetShortsByConsumerDto> getMainShorts(Pageable pageable) {
    return shortsRepository.findShortsByIsDeletedAndIsActivate(false, true, pageable).stream()
        .map(shorts -> GetShortsByConsumerDto.toDto(shorts))
        .collect(Collectors.toList());
  }


  public List<GetShortsByConsumerDto> getOneSellerShorts(Long sellerId, Pageable pageable) {
    return shortsRepository.findShortsBySellerIdAndIsDeletedAndIsActivate(sellerId, false, true, pageable).stream()
            .map(shorts -> GetShortsByConsumerDto.toDto(shorts))
            .collect(Collectors.toList());
  }


}
