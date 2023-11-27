package com.jeontongju.product.repository;

import com.jeontongju.product.entity.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductRepositoryTest {

  @Autowired private CategoryRepository categoryRepository;
  @Autowired private ProductRepository productRepository;
  @Autowired private ProductThumbnailImageRepository productThumbnailImageRepository;
  @Autowired private ProductDetailsImageRepository productDetailsImageRepository;

  private Category initCategory() {
    Category category = Category.builder().name("막걸리").build();
    return categoryRepository.save(category);
  }

  private Product initProduct() {
    Category category = initCategory();
    ProductThumbnailImage productThumbnailImageEntity =
        ProductThumbnailImage.builder().imageUrl("/thumbnail/example").build();
    ProductDetailsImage productDetailsImageEntity =
        ProductDetailsImage.builder().imageUrl("/details/example").build();

    Long price = 10000L;
    Long capacity = 936L;

    Product productEntity =
        Product.builder()
            .name("복순도가")
            .category(category)
            .sellerId(1L)
            .price(price)
            .capacity(capacity)
            .capacityToPriceRatio(Math.round(((double) price / capacity) * 100))
            .description("복순복순")
            .alcoholDegree(17.1)
            .breweryName("복순 양조장")
            .breweryAddress("서울특별시 강동구")
            .breweryZoneCode("01111")
            .breweryAddressDetails("105동")
            .manufacturer("우리도가")
            .stockQuantity(100L)
            .productThumbnailImage(productThumbnailImageEntity)
            .productDetailsImage(productDetailsImageEntity)
            .storeName("우리우리도가")
            .storeImageUrl("/store/example")
            .build();

    return productRepository.save(productEntity);
  }

  @Test
  @DisplayName("TEST - create category")
  void createCategory() {

    Category categoryEntity = Category.builder().name("소주").build();
    Category category = categoryRepository.save(categoryEntity);
    Assertions.assertThat(category.getCategoryId()).isNotNull();
    Assertions.assertThat(category.getName()).isSameAs(categoryEntity.getName());
  }

  @Test
  @DisplayName("TEST - create product, productThumbnail, productDetails")
  void createProduct() {

    Category category = initCategory();
    ProductThumbnailImage productThumbnailImageEntity =
        ProductThumbnailImage.builder().imageUrl("/thumbnail/example").build();
    ProductDetailsImage productDetailsImageEntity =
        ProductDetailsImage.builder().imageUrl("/details/example").build();

    Long price = 10000L;
    Long capacity = 936L;

    Product productEntity =
        Product.builder()
            .name("복순도가")
            .category(category)
            .sellerId(1L)
            .price(price)
            .capacity(capacity)
            .capacityToPriceRatio(Math.round(((double) price / capacity) * 100))
            .description("복순복순")
            .alcoholDegree(17.1)
            .breweryName("복순 양조장")
            .breweryAddress("서울특별시 강동구")
            .breweryZoneCode("01111")
            .breweryAddressDetails("105동")
            .manufacturer("우리도가")
            .stockQuantity(100L)
            .productThumbnailImage(productThumbnailImageEntity)
            .productDetailsImage(productDetailsImageEntity)
            .storeName("우리우리도가")
            .storeImageUrl("/store/example")
            .build();

    Product product = productRepository.save(productEntity);

    Assertions.assertThat(product.getProductId()).isNotNull();
    Assertions.assertThat(product.getName()).isSameAs(productEntity.getName());
    Assertions.assertThat(product.getReviewCount()).isSameAs(0L);
    Assertions.assertThat(product.getIsActivate()).isSameAs(true);
    Assertions.assertThat(
            productThumbnailImageRepository
                .findById(product.getProductThumbnailImage().getProductThumbnailImageId())
                .get()
                .getImageUrl())
        .isSameAs(productThumbnailImageEntity.getImageUrl());

    Assertions.assertThat(
            productDetailsImageRepository
                .findById(product.getProductDetailsImage().getProductDetailsImageId())
                .get()
                .getImageUrl())
        .isSameAs(productDetailsImageEntity.getImageUrl());
  }

  @Test
  @DisplayName("TEST - update productImage at Product")
  void updateProductImage() {

    String newProductThumbnail = "/thumbnail/new-example";
    String newProductDetails = "/details/new-example";

    Product product = initProduct();
    Product findProduct = productRepository.findById(product.getProductId()).get();

    findProduct.getProductThumbnailImage().setImageUrl(newProductThumbnail);
    findProduct.getProductDetailsImage().setImageUrl(newProductDetails);

    Assertions.assertThat(
            productThumbnailImageRepository
                .findById(product.getProductThumbnailImage().getProductThumbnailImageId())
                .get()
                .getImageUrl())
        .isSameAs(newProductThumbnail);

    Assertions.assertThat(
            productDetailsImageRepository
                .findById(product.getProductDetailsImage().getProductDetailsImageId())
                .get()
                .getImageUrl())
        .isSameAs(newProductDetails);
  }
}
