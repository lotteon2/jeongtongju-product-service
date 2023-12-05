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

  private String productName;

  private String productDescription;

  private String productThumbnailImageUrl;

  private Double productAlcoholDegree;

  private Long productCapacity;

  private String breweryName;

  private String breweryZonecode;

  private String breweryAddress;

  private String breweryAddressDetails;

  private String manufacturer;

  private Long productPrice;

  private Long registeredQuantity;

  private String productDetailsImageUrl;

  private Long category;

  private Long totalSalesCount;

  private Long reviewCount;

  private String storeName;

  private String storeImageUrl;

  private Boolean isActivate;

  private Boolean isDeleted;

  public static ProductRecodeContents toDto(String productId, Product savedProduct) {

    return ProductRecodeContents.builder()
        .productId(productId)
        .productName(savedProduct.getName())
        .productDescription(savedProduct.getDescription())
        .productThumbnailImageUrl(savedProduct.getProductThumbnailImage().getImageUrl())
        .productDetailsImageUrl(savedProduct.getProductDetailsImage().getImageUrl())
        .productAlcoholDegree(savedProduct.getAlcoholDegree())
        .productCapacity(savedProduct.getCapacity())
        .breweryName(savedProduct.getBreweryName())
        .breweryZonecode(savedProduct.getBreweryZoneCode())
        .breweryAddress(savedProduct.getBreweryAddress())
        .breweryAddressDetails(savedProduct.getBreweryAddressDetails())
        .manufacturer(savedProduct.getManufacturer())
        .registeredQuantity(savedProduct.getStockQuantity())
        .category(savedProduct.getCategory().getCategoryId())
        .totalSalesCount(savedProduct.getTotalSalesCount())
        .reviewCount(savedProduct.getReviewCount())
        .storeName(savedProduct.getStoreName())
        .storeImageUrl(savedProduct.getStoreImageUrl())
        .isActivate(savedProduct.getIsActivate())
        .isDeleted(savedProduct.getIsDeleted())
        .build();
  }
}
