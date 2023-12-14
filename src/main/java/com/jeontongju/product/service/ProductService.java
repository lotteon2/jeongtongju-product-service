package com.jeontongju.product.service;

import com.jeontongju.product.client.SellerServiceClient;
import com.jeontongju.product.domain.Category;
import com.jeontongju.product.domain.Product;
import com.jeontongju.product.dto.request.ModifyProductInfoDto;
import com.jeontongju.product.dto.request.ProductDto;
import com.jeontongju.product.dto.response.CategoryDto;
import com.jeontongju.product.dto.response.GetProductInfoDto;
import com.jeontongju.product.dto.temp.ProductInfoDto;
import com.jeontongju.product.dto.temp.ProductSearchDto;
import com.jeontongju.product.dto.temp.ProductUpdateDto;
import com.jeontongju.product.dto.temp.SellerInfoDto;
import com.jeontongju.product.dynamodb.domian.ProductProductRecordAdditionalContents;
import com.jeontongju.product.dynamodb.domian.ProductRecord;
import com.jeontongju.product.dynamodb.domian.ProductRecordContents;
import com.jeontongju.product.dynamodb.domian.ProductRecordId;
import com.jeontongju.product.dynamodb.repository.ProductRecordRepository;
import com.jeontongju.product.exception.CategoryNotFoundException;
import com.jeontongju.product.exception.ProductNotFoundException;
import com.jeontongju.product.exception.common.ProductOrderException;
import com.jeontongju.product.kafka.ProductProducer;
import com.jeontongju.product.mapper.ProductMapper;
import com.jeontongju.product.repository.CategoryRepository;
import com.jeontongju.product.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
  private final ProductRecordRepository productRecodeRepository;
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
            productMapper.toEntity(productDto, memberId, category, sellerInfoDto));

    // dynamoDB save
    ProductRecordContents createProductRecode =
        ProductRecordContents.toDto(savedProduct.getProductId(), savedProduct, null);

    ProductProductRecordAdditionalContents createProductRecodeAdditionalContents =
        ProductProductRecordAdditionalContents.toDto(savedProduct.getProductId(), productDto);

    productRecodeRepository.save(
        ProductRecord.builder()
            .productRecodeId(
                ProductRecordId.builder()
                    .productId(savedProduct.getProductId())
                    .createdAt(LocalDateTime.now().toString())
                    .build())
            .productRecode(createProductRecode)
            .productRecodeAdditionalContents(createProductRecodeAdditionalContents)
            .action("INSERT")
            .build());

    // kafka - search
    productProducer.sendCreateProductToSearch(createProductRecode);

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
    productProducer.sendDeleteProductToSearch(productId);
    productProducer.sendDeleteProductToWish(productId);
    productProducer.sendDeleteProductToReview(productId);
  }

  @Transactional
  public void modifyProductByAdmin(String productId, Boolean isActivate) {
    Product product =
        productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
    product.setActivate(isActivate);

    ProductRecordContents updateProductRecode =
        ProductRecordContents.toDto(productId, product, null);
    productRecodeRepository.save(
        ProductRecord.builder()
            .productRecodeId(
                ProductRecordId.builder()
                    .productId(productId)
                    .createdAt(product.getUpdatedAt().toString())
                    .build())
            .productRecode(updateProductRecode)
            .action("UPDATE")
            .build());

    // kafka search
    productProducer.sendUpdateProductToSearch(updateProductRecode);
  }

  @Transactional
  public void modifyProductBySeller(String productId, ModifyProductInfoDto modifyProductInfoDto) {
    Product product =
        productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
    product.modifyProduct(modifyProductInfoDto);

    // 변경 이력 - dynamo db
    ProductRecordContents updateProductRecode =
        ProductRecordContents.toDto(productId, product, null);

    productRecodeRepository.save(
        ProductRecord.builder()
            .productRecodeId(
                ProductRecordId.builder()
                    .productId(productId)
                    .createdAt(LocalDateTime.now().toString())
                    .build())
            .productRecode(updateProductRecode)
            .action("UPDATE")
            .build());

    // kafka review, search
    productProducer.sendUpdateProductToSearch(updateProductRecode);
    productProducer.sendUpdateProductToReview(modifyProductInfoDto.getProductThumbnailImageUrl());
  }

  public List<ProductInfoDto> getProductInfoForOrder(ProductSearchDto productSearchDto) {

    List<ProductInfoDto> productInfoDtoList = new ArrayList<>();
    long actualTotalPrice = 0L;
    for (ProductUpdateDto orderedProduct : productSearchDto.getProductUpdateDtoList()) {

      ProductInfoDto productInfoDto =
          productRepository
              .findByProductIdsForOrder(orderedProduct.getProductId())
              .orElseThrow(ProductNotFoundException::new);

      if (productInfoDto.getProductCount() < orderedProduct.getProductCount()) {
        throw new ProductOrderException("재고가 부족합니다.");
      }
      actualTotalPrice += productInfoDto.getProductPrice() * orderedProduct.getProductCount();
      productInfoDtoList.add(productInfoDto);
    }

    if (actualTotalPrice != productSearchDto.getTotalPrice()) {
      throw new ProductOrderException("총 가격이 일치하지 않습니다.");
    }

    return productInfoDtoList;
  }
}
