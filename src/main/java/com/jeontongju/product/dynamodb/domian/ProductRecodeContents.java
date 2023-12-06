package com.jeontongju.product.dynamodb.domian;

import com.jeontongju.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRecodeContents {

  private String productId;

  private String name;

  private String description;

  private String productThumbnailImageUrl;

  private Double alcoholDegree;

  private Long capacity;

  private String breweryName;

  private String breweryZonecode;

  private String breweryAddress;

  private String breweryAddressDetails;

  private String manufacturer;

  private Long price;

  private Long capacityToPriceRatio;

  private Long stockQuantity;

  private Long reviewCount;

  private Long totalSalesCount;

  private String storeName;

  private String storeImageUrl;

  private String productDetailsImageUrl;

  private Long categoryId;

  private Long sellerId;

  private String createdAt;

  private String updatedAt;

  private Boolean isActivate;

  private Boolean isDeleted;

  public static ProductRecodeContents toDto(String productId, Product savedProduct) {

    return ProductRecodeContents.builder()
        .productId(productId)
        .name(savedProduct.getName())
        .description(savedProduct.getDescription())
        .productThumbnailImageUrl(savedProduct.getProductThumbnailImage().getImageUrl())
        .productDetailsImageUrl(savedProduct.getProductDetailsImage().getImageUrl())
        .alcoholDegree(savedProduct.getAlcoholDegree())
        .price(savedProduct.getPrice())
        .capacityToPriceRatio(savedProduct.getCapacityToPriceRatio())
        .capacity(savedProduct.getCapacity())
        .breweryName(savedProduct.getBreweryName())
        .breweryZonecode(savedProduct.getBreweryZoneCode())
        .breweryAddress(savedProduct.getBreweryAddress())
        .breweryAddressDetails(savedProduct.getBreweryAddressDetails())
        .manufacturer(savedProduct.getManufacturer())
        .stockQuantity(savedProduct.getStockQuantity())
        .categoryId(savedProduct.getCategory().getCategoryId())
        .sellerId(savedProduct.getSellerId())
        .totalSalesCount(savedProduct.getTotalSalesCount())
        .reviewCount(savedProduct.getReviewCount())
        .storeName(savedProduct.getStoreName())
        .storeImageUrl(savedProduct.getStoreImageUrl())
        .createdAt(savedProduct.getCreatedAt().toString())
        .updatedAt(savedProduct.getUpdatedAt().toString())
        .isActivate(savedProduct.getIsActivate())
        .isDeleted(savedProduct.getIsDeleted())
        .build();
  }
}
