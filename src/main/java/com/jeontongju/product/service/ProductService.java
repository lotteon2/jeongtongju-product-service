package com.jeontongju.product.service;

import com.jeontongju.product.client.SellerServiceClient;
import com.jeontongju.product.domain.Category;
import com.jeontongju.product.domain.Product;
import com.jeontongju.product.dto.request.ProductDto;
import com.jeontongju.product.dto.response.CategoryDto;
import com.jeontongju.product.dto.response.MyProductInfoDto;
import com.jeontongju.product.dto.temp.SellerInfoDto;
import com.jeontongju.product.dynamodb.domian.ProductRecode;
import com.jeontongju.product.dynamodb.domian.ProductRecodeContents;
import com.jeontongju.product.dynamodb.domian.ProductRecodeId;
import com.jeontongju.product.dynamodb.repository.ProductRecodeRepository;
import com.jeontongju.product.exception.CategoryNotFoundException;
import com.jeontongju.product.exception.ProductNotFoundException;
import com.jeontongju.product.exception.common.FeignServerException;
import com.jeontongju.product.kafka.ProductProducer;
import com.jeontongju.product.mapper.ProductMapper;
import com.jeontongju.product.repository.CategoryRepository;
import com.jeontongju.product.repository.ProductRepository;
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
  private final ProductRecodeRepository productRecodeRepository;
  private final ProductMapper productMapper;
  private final ProductProducer productProducer;

  public List<CategoryDto> getCategoryAll() {
    return categoryRepository.findAll().stream()
        .map(CategoryDto::toDto)
        .collect(Collectors.toList());
  }

  private void sellerFeignServerException(Exception exception) {
    log.error(exception.getMessage());
    throw new FeignServerException("seller-service 가 정상적으로 동작하지 않습니다.");
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
        productRepository.save(productMapper.toEntity(productDto, memberId, category, sellerInfoDto));

    ProductRecodeContents createProductRecode =
        ProductRecodeContents.toDto(savedProduct.getProductId(), productDto, savedProduct);

    // dynamoDB save
    productRecodeRepository.save(
        ProductRecode.builder()
            .productRecodeId(
                ProductRecodeId.builder()
                    .productId(savedProduct.getProductId())
                    .createdAt(savedProduct.getCreatedAt().toString())
                    .build())
            .createProductRecode(createProductRecode)
            .action("INSERT")
            .build());

    // kafka - search
    productProducer.sendCreateProduct(createProductRecode);

    return savedProduct;
  }

  public List<MyProductInfoDto> getMyProductInfo(Long memberId) {

    return productRepository.findBySellerIdWithoutIsDeleted(memberId).stream()
        .map(product -> MyProductInfoDto.toDto(product))
        .collect(Collectors.toList());
  }
  
  @Transactional
  public void deleteProduct(String productId) {

    Product product =
        productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
    product.setDeleted(true);
    productProducer.sendDeleteProduct(productId);
  }
}
