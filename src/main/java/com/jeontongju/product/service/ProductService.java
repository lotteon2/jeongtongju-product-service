package com.jeontongju.product.service;

import com.jeontongju.product.client.SellerServiceClient;
import com.jeontongju.product.domain.Category;
import com.jeontongju.product.domain.Product;
import com.jeontongju.product.dto.request.ModifyProductInfoDto;
import com.jeontongju.product.dto.request.ProductDto;
import com.jeontongju.product.dto.response.CategoryDto;
import com.jeontongju.product.dto.response.GetProductInfoDto;
import com.jeontongju.product.dynamodb.domian.*;
import com.jeontongju.product.dynamodb.repository.ProductMetricsRepository;
import com.jeontongju.product.dynamodb.repository.ProductRecordRepository;
import com.jeontongju.product.exception.CategoryNotFoundException;
import com.jeontongju.product.exception.ProductNotFoundException;
import com.jeontongju.product.exception.StockException;
import com.jeontongju.product.exception.common.CustomFailureFeignException;
import com.jeontongju.product.kafka.ProductProducer;
import com.jeontongju.product.mapper.ProductMapper;
import com.jeontongju.product.repository.CategoryRepository;
import com.jeontongju.product.repository.ProductRepository;
import io.github.bitbox.bitbox.dto.*;
import io.github.bitbox.bitbox.enums.FailureTypeEnum;
import io.github.bitbox.bitbox.enums.NotificationTypeEnum;
import io.github.bitbox.bitbox.enums.RecipientTypeEnum;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

  private final CategoryRepository categoryRepository;
  private final ProductRepository productRepository;
  private final SellerServiceClient sellerServiceClient;
  private final ProductRecordRepository productRecordRepository;
  private final ProductMetricsRepository productMetricsRepository;
  private final ProductMapper productMapper;
  private final ProductProducer productProducer;

  public List<CategoryDto> getCategoryAll() {
    return categoryRepository.findAll().stream()
        .map(CategoryDto::toDto)
        .collect(Collectors.toList());
  }

  @Transactional
  public Product createProduct(Long memberId, ProductDto productDto) {

    Category category =
        categoryRepository
            .findById(productDto.getCategoryId())
            .orElseThrow(CategoryNotFoundException::new);

    // feign - seller
    SellerInfoDto sellerInfoDto =
        sellerServiceClient.getSellerInfoForCreateProduct(memberId).getData();

    // rdb save
    Product savedProduct =
        productRepository.save(
            productMapper.toProductEntity(productDto, memberId, category, sellerInfoDto));

    // dynamoDB save
    ProductRecordContents createProductRecord =
        ProductRecordContents.toDto(savedProduct.getProductId(), savedProduct, null);

    ProductRecordAdditionalContents createproductRecordAdditionalContents =
        ProductRecordAdditionalContents.toDto(savedProduct.getProductId(), productDto);

    productRecordRepository.save(
        ProductRecord.builder()
            .productRecordId(
                ProductRecordId.builder()
                    .productId(savedProduct.getProductId())
                    .createdAt(LocalDateTime.now().toString())
                    .build())
            .productRecord(createProductRecord)
            .productRecordAdditionalContents(createproductRecordAdditionalContents)
            .action("INSERT")
            .build());

    productMetricsRepository.save(
        ProductMetrics.builder()
            .productMetricsId(
                ProductMetricsId.builder()
                    .productId(savedProduct.getProductId())
                    .createdAt(LocalDateTime.now().toString())
                    .build())
            .sellerId(savedProduct.getSellerId())
            .action("INSERT")
            .reviewCount(0L)
            .totalSalesCount(0L)
            .totalSalesPrice(0L)
            .build());

    return savedProduct;
  }

  public List<GetProductInfoDto> getProductInfo(Long memberId) {

    return productRepository.findBySellerIdWithoutIsDeleted(memberId).stream()
        .map(product -> GetProductInfoDto.toDto(product))
        .collect(Collectors.toList());
  }

  @Transactional
  public void deleteProduct(String productId) {

    Product product =
        productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
    product.setDeleted(true);
    List<String> productIds = List.of(productId);
    productProducer.sendDeleteProductToWish(productIds);
    productProducer.sendDeleteProductToReview(productIds);

    ProductRecordContents updateProductRecord =
            ProductRecordContents.toDto(productId, product, null);

    productRecordRepository.save(
            ProductRecord.builder()
                    .productRecordId(
                            ProductRecordId.builder()
                                    .productId(productId)
                                    .createdAt(product.getUpdatedAt().toString())
                                    .build())
                    .productRecord(updateProductRecord)
                    .action("UPDATE")
                    .build());

  }

  @Transactional
  public void deleteProductByDeleteSeller(Long memberId) {

    List<Product> products = productRepository.findBySellerId(memberId);
    List<String> productIds = new ArrayList<>();

    products.forEach(
        product -> {
          product.setDeleted(true);
          productIds.add(product.getProductId());

          ProductRecordContents updateProductRecord =
                  ProductRecordContents.toDto(product.getProductId(), product, null);

          productRecordRepository.save(
                  ProductRecord.builder()
                          .productRecordId(
                                  ProductRecordId.builder()
                                          .productId(product.getProductId())
                                          .createdAt(product.getUpdatedAt().toString())
                                          .build())
                          .productRecord(updateProductRecord)
                          .action("UPDATE")
                          .build());

        });

    productProducer.sendDeleteProductToWish(productIds);
    productProducer.sendDeleteProductToReview(productIds);
  }

  @Transactional
  public void modifyProductByAdmin(String productId, Boolean isActivate) {
    Product product =
        productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
    product.setActivate(isActivate);

    ProductRecordContents updateProductRecord =
        ProductRecordContents.toDto(productId, product, null);
    productRecordRepository.save(
        ProductRecord.builder()
            .productRecordId(
                ProductRecordId.builder()
                    .productId(productId)
                    .createdAt(product.getUpdatedAt().toString())
                    .build())
            .productRecord(updateProductRecord)
            .action("UPDATE")
            .build());
  }

  @Transactional
  public void modifyProductBySeller(String productId, ModifyProductInfoDto modifyProductInfoDto) {
    Product product =
        productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
    product.modifyProduct(modifyProductInfoDto);

    // 변경 이력 - dynamo db
    ProductRecordContents updateProductRecord =
        ProductRecordContents.toDto(productId, product, null);

    productRecordRepository.save(
        ProductRecord.builder()
            .productRecordId(
                ProductRecordId.builder()
                    .productId(productId)
                    .createdAt(LocalDateTime.now().toString())
                    .build())
            .productRecord(updateProductRecord)
            .action("UPDATE")
            .build());

    // kafka review
    if (modifyProductInfoDto.getProductThumbnailImageUrl() != null) {
      productProducer.sendUpdateProductToReview(
          ProductImageInfoDto.builder()
              .productId(productId)
              .productThumbnailImageUrl(modifyProductInfoDto.getProductThumbnailImageUrl())
              .build());
    }
  }

  @Transactional
  public void modifyProductByModifySeller(SellerInfoDto sellerInfoDto) {

    List<Product> products = productRepository.findBySellerId(sellerInfoDto.getSellerId());

    for (Product product : products) {
      product.modifyProductByModifySeller(sellerInfoDto);
      ProductRecordContents updateProductRecord =
          ProductRecordContents.toDto(product.getProductId(), product, null);

      productRecordRepository.save(
          ProductRecord.builder()
              .productRecordId(
                  ProductRecordId.builder()
                      .productId(product.getProductId())
                      .createdAt(LocalDateTime.now().toString())
                      .build())
              .productRecord(updateProductRecord)
              .action("UPDATE")
              .build());
    }
  }

  public List<ProductInfoDto> getProductInfoForOrder(ProductSearchDto productSearchDto) {

    List<ProductInfoDto> productInfoDtoList = new ArrayList<>();
    long actualTotalPrice = 0L;
    for (ProductUpdateDto orderedProduct : productSearchDto.getProductUpdateDtoList()) {

      Product product =
          productRepository
              .findByProductIdsForOrder(orderedProduct.getProductId())
              .orElseThrow(ProductNotFoundException::new);

      ProductInfoDto productInfoDto = productMapper.toProductInfoDto(product, orderedProduct);

      if (product.getStockQuantity() < orderedProduct.getProductCount()) {
        log.error(FailureTypeEnum.LACK_OF_STOCK.getValue());
        throw new CustomFailureFeignException(FailureTypeEnum.LACK_OF_STOCK);
      }
      actualTotalPrice += productInfoDto.getProductPrice() * orderedProduct.getProductCount();
      productInfoDtoList.add(productInfoDto);
    }

    if (actualTotalPrice != productSearchDto.getTotalPrice()) {
      log.error(FailureTypeEnum.MISMATCH_TOTAL_PRODUCT_AMOUNT.getValue());
      throw new CustomFailureFeignException(FailureTypeEnum.MISMATCH_TOTAL_PRODUCT_AMOUNT);
    }

    return productInfoDtoList;
  }

  @Transactional
  public void reduceStock(List<ProductUpdateDto> productUpdateDtoList) {
    for (ProductUpdateDto productUpdateDto : productUpdateDtoList) {

      Product product =
          productRepository
              .findByProductIdForUpdateStock(productUpdateDto.getProductId()) // pessimistic lock
              .orElseThrow(() -> new StockException("존재 하지 않는 상품"));

      if (productUpdateDto.getProductCount() > product.getStockQuantity()) {
        throw new StockException("재고 부족");
      }
      // 재고 차감
      product.setStockQuantity(product.getStockQuantity() - productUpdateDto.getProductCount());

      // 변경 이력 - dynamo db
      ProductRecordContents updateProductRecord =
          ProductRecordContents.toDto(product.getProductId(), product, null);

      productRecordRepository.save(
          ProductRecord.builder()
              .productRecordId(
                  ProductRecordId.builder()
                      .productId(product.getProductId())
                      .createdAt(LocalDateTime.now().toString())
                      .build())
              .productRecord(updateProductRecord)
              .action("UPDATE")
              .build());
    }
  }

  @Transactional
  public void rollbackStock(List<ProductUpdateDto> productUpdateDtoList) {
    for (ProductUpdateDto productUpdateDto : productUpdateDtoList) {
      Product product =
          productRepository
              .findByProductIdForUpdateStock(productUpdateDto.getProductId()) // pessimistic lock
              .orElseThrow(() -> new StockException("존재 하지 않는 상품"));
      // 재고 복구
      product.setStockQuantity(product.getStockQuantity() + productUpdateDto.getProductCount());

      // 변경 이력 - dynamo db
      ProductRecordContents updateProductRecord =
          ProductRecordContents.toDto(product.getProductId(), product, null);

      productRecordRepository.save(
          ProductRecord.builder()
              .productRecordId(
                  ProductRecordId.builder()
                      .productId(product.getProductId())
                      .createdAt(LocalDateTime.now().toString())
                      .build())
              .productRecord(updateProductRecord)
              .action("UPDATE")
              .build());
    }
  }

  @Transactional
  public void addProductMetricsFromOrder(List<ProductUpdateDto> productUpdateDtoList) {
    productUpdateDtoList.forEach(
        productUpdateDto -> {
          Product product =
              productRepository
                  .findById(productUpdateDto.getProductId())
                  .orElseThrow(ProductNotFoundException::new);

          productMetricsRepository.save(
              ProductMetrics.builder()
                  .productMetricsId(
                      ProductMetricsId.builder()
                          .productId(productUpdateDto.getProductId())
                          .createdAt(LocalDateTime.now().toString())
                          .build())
                  .action("ORDER")
                  .sellerId(product.getSellerId())
                  .reviewCount(0L)
                  .totalSalesCount(productUpdateDto.getProductCount())
                  .totalSalesPrice(product.getPrice() * productUpdateDto.getProductCount())
                  .build());
        });
  }

  @Transactional
  public void reduceProductMetricsFromCancelOrder(List<ProductUpdateDto> productUpdateDtoList) {

    productUpdateDtoList.forEach(
        productUpdateDto -> {
          Product product =
              productRepository
                  .findById(productUpdateDto.getProductId())
                  .orElseThrow(ProductNotFoundException::new);

          productMetricsRepository.save(
              ProductMetrics.builder()
                  .productMetricsId(
                      ProductMetricsId.builder()
                          .productId(productUpdateDto.getProductId())
                          .createdAt(LocalDateTime.now().toString())
                          .build())
                  .action("CANCEL_ORDER")
                  .sellerId(product.getSellerId())
                  .reviewCount(0L)
                  .totalSalesCount(-productUpdateDto.getProductCount())
                  .totalSalesPrice(-(product.getPrice() * productUpdateDto.getProductCount()))
                  .build());
        });
  }

  public SellerProductInfoDto getProductSeller(String productId) {

    Product product =
        productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

    return SellerProductInfoDto.builder()
        .sellerId(product.getSellerId())
        .productImageUrl(product.getProductThumbnailImage().getImageUrl())
        .build();
  }

  public List<ProductWishInfoDto> getProductInfoForWish(ProductIdListDto productIdList) {

    return productIdList.getProductIdList().stream()
        .map(
            id ->
                productMapper.toProductWishInfoDto(
                    productRepository.findById(id).orElseThrow(ProductNotFoundException::new)))
        .collect(Collectors.toList());
  }

  public HashMap<String, Long> getProductStockByCart(ProductIdListDto productIdList) {

    HashMap<String, Long> productStock = new HashMap<>();

    productIdList.getProductIdList().stream()
        .forEach(
            id -> {
              log.info(
                  productRepository
                      .findById(id)
                      .orElseThrow(ProductNotFoundException::new)
                      .getStockQuantity()
                      .toString());
              productStock.put(
                  id,
                  productRepository
                      .findById(id)
                      .orElseThrow(ProductNotFoundException::new)
                      .getStockQuantity());
            });

    return productStock;
  }

  public void checkProductStock(List<ProductUpdateDto> productUpdateListDto) {
    for (ProductUpdateDto productUpdateDto : productUpdateListDto) {

      Product product = productRepository.findById(productUpdateDto.getProductId()).get();
      if (product != null & product.getStockQuantity() < 6) {
        productProducer.sendNotification(
            MemberInfoForNotificationDto.builder()
                .recipientId(product.getSellerId())
                .notificationType(NotificationTypeEnum.OUT_OF_STOCK)
                .recipientType(RecipientTypeEnum.ROLE_SELLER)
                .createdAt(LocalDateTime.now())
                .build());
      }
    }
  }

  public Long getStockUnderFive(Long sellerId) {
    Long countProductUnderFive = 0L;
    List<Product> productList = productRepository.findBySellerId(sellerId);

    for (Product product : productList) {
      if (product.getStockQuantity() < 5) {
        countProductUnderFive += 1;
      }
    }
    return countProductUnderFive;
  }
}
