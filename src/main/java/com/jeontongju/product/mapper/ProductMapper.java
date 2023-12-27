package com.jeontongju.product.mapper;

import com.jeontongju.product.domain.*;
import com.jeontongju.product.dto.request.CreateShortsDto;
import com.jeontongju.product.dto.request.ProductDto;
import com.jeontongju.product.enums.ShortsTypeEnum;
import io.github.bitbox.bitbox.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {
  public Product toProductEntity(
      ProductDto productDto, Long sellerId, Category category, SellerInfoDto sellerInfoDto) {

    ProductThumbnailImage productThumbnailImage =
        ProductThumbnailImage.builder().imageUrl(productDto.getProductThumbnailImageUrl()).build();
    ProductDetailsImage productDetailsImage =
        ProductDetailsImage.builder().imageUrl(productDto.getProductDetailsImageUrl()).build();
    Product product =
        Product.builder()
            .category(category)
            .sellerId(sellerId)
            .name(productDto.getProductName())
            .price(productDto.getProductPrice())
            .capacityToPriceRatio(
                Math.round(
                    ((double) productDto.getProductPrice() / productDto.getProductCapacity())
                        * 100))
            .description(productDto.getProductDescription())
            .alcoholDegree(productDto.getProductAlcoholDegree())
            .capacity(productDto.getProductCapacity())
            .breweryName(productDto.getBreweryName())
            .breweryZoneCode(productDto.getBreweryZonecode())
            .breweryAddress(productDto.getBreweryAddress())
            .breweryAddressDetails(productDto.getBreweryAddressDetails())
            .manufacturer(productDto.getManufacturer())
            .stockQuantity(productDto.getRegisteredQuantity())
            .productThumbnailImage(productThumbnailImage)
            .productDetailsImage(productDetailsImage)
            .storeName(sellerInfoDto.getStoreName())
            .storeImageUrl(sellerInfoDto.getStoreImageUrl())
            .build();

    productThumbnailImage.setProduct(product);
    productDetailsImage.setProduct(product);

    return product;
  }

  public Shorts toShortsEntity(Long sellerId, CreateShortsDto createShortsDto) {
    return Shorts.builder()
        .sellerId(sellerId)
        .video(createShortsDto.getShortsVideoUrl())
        .preview(createShortsDto.getShortsPreviewUrl())
        .thumbnail(createShortsDto.getShortsThumbnailUrl())
        .title(createShortsDto.getShortsTitle())
        .description(createShortsDto.getShortsDescription())
        .productId(createShortsDto.getProductId())
        .type(
            createShortsDto.getProductId() == null ? ShortsTypeEnum.SELLER : ShortsTypeEnum.PRODUCT)
        .build();
  }

  public ProductInfoDto toProductInfoDto(Product product, ProductUpdateDto productUpdateDto) {

    return ProductInfoDto.builder()
        .productId(product.getProductId())
        .productName(product.getName())
        .productPrice(product.getPrice())
        .productCount(productUpdateDto.getProductCount())
        .sellerId(product.getSellerId())
        .sellerName(product.getStoreName())
        .productImg(product.getProductThumbnailImage().getImageUrl())
        .build();
  }

  public ProductWishInfoDto toProductWishInfoDto(Product product) {

    return ProductWishInfoDto.builder()
        .productId(product.getProductId())
        .productName(product.getName())
        .productPrice(product.getPrice())
        .productThumbnailImage(product.getProductThumbnailImage().getImageUrl())
        .stockQuantity(product.getStockQuantity())
        .isActivate(product.getIsActivate())
        .isDeleted(product.getIsDeleted())
        .build();
  }
}
