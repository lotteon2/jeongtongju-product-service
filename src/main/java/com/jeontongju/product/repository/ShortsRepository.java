package com.jeontongju.product.repository;

import com.jeontongju.product.domain.Shorts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortsRepository extends JpaRepository<Shorts, Long> {

    Page<Shorts> findShortsByIsDeletedAndIsActivate(boolean isDeleted, boolean isActivate, Pageable pageable);
    Page<Shorts> findShortsBySellerIdAndIsDeletedAndIsActivate(Long sellerId, boolean isDeleted, boolean isActivate, Pageable pageable);
    Page<Shorts> findShortsBySellerId(Long sellerId, Pageable pageable, boolean isDeleted);
}
