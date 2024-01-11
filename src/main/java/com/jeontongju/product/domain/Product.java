package com.jeontongju.product.domain;

import com.jeontongju.product.domain.common.BaseEntity;
import com.jeontongju.product.dto.request.ModifyProductInfoDto;
import io.github.bitbox.bitbox.dto.SellerInfoDto;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
@Table(name = "product")
public class Product extends BaseEntity {

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "product_id", columnDefinition = "CHAR(36)")
  private String productId;

  @JoinColumn(name = "category_id")
  @ManyToOne(targetEntity = Category.class, fetch = FetchType.LAZY)
  private Category category;

  @Column(name = "seller_id", nullable = false)
  private Long sellerId;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "price", nullable = false)
  private Long price;

  @Column(name = "capacity_to_price_ratio", nullable = false)
  private Long capacityToPriceRatio;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "alcohol_degree", nullable = false)
  private Double alcoholDegree;

  @Column(name = "capacity", nullable = false)
  private Long capacity;

  @Column(name = "brewery_name", nullable = false)
  private String breweryName;

  @Column(name = "brewery_zonecode", nullable = false)
  private String breweryZoneCode;

  @Column(name = "brewery_address", nullable = false)
  private String breweryAddress;

  @Column(name = "brewery_address_details")
  private String breweryAddressDetails;

  @Column(name = "manufacturer", nullable = false)
  private String manufacturer;

  @Column(name = "stock_quantity", nullable = false)
  private Long stockQuantity;

  @OneToOne(
      mappedBy = "product",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private ProductThumbnailImage productThumbnailImage;

  @OneToOne(
      mappedBy = "product",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private ProductDetailsImage productDetailsImage;

  @Column(name = "store_image_url", nullable = false)
  private String storeImageUrl;

  @Column(name = "store_name", nullable = false)
  private String storeName;

  @Builder.Default
  @Column(name = "is_activate", nullable = false)
  private Boolean isActivate = true;

  @Builder.Default
  @Column(name = "is_deleted", nullable = false)
  private Boolean isDeleted = false;

  public void setName(String name) {
    if (name != null) {
      this.name = name;
    }
  }

  public void setPrice(Long price) {
    if (price != null) {
      this.price = price;
      this.capacityToPriceRatio = Math.round(((double) price / this.capacity * 100));
    }
  }

  public void setStockQuantity(Long stockQuantity) {
    if (stockQuantity != null) {
      this.stockQuantity = stockQuantity;
    }
  }

  public void setProductThumbnailImage(String productThumbnailImageUrl) {
    if (productThumbnailImageUrl != null) {
      this.productThumbnailImage.setImageUrl(productThumbnailImageUrl);
    }
  }

  public void setProductDetailsImage(String productDetailsImageUrl) {
    if (productDetailsImageUrl != null) {
      this.productDetailsImage.setImageUrl(productDetailsImageUrl);
    }
  }

  public void setActivate(Boolean activate) {
    if (activate != null) {
      this.isActivate = activate;
    }
  }

  public void setStoreImageUrl(String storeImageUrl) {
    if (storeImageUrl != null) {
      this.storeImageUrl = storeImageUrl;
    }
  }

  public void setStoreName(String storeName) {
    if (storeName != null) {
      this.storeName = storeName;
    }
  }

  public void setDeleted(Boolean deleted) {
    this.isDeleted = deleted;
  }

  public void modifyProduct(ModifyProductInfoDto modifyProductInfoDto) {
    setName(modifyProductInfoDto.getProductName());
    setPrice(modifyProductInfoDto.getProductPrice());
    setProductThumbnailImage(modifyProductInfoDto.getProductThumbnailImageUrl());
    setProductDetailsImage(modifyProductInfoDto.getProductDetailsImageUrl());
    setStockQuantity(modifyProductInfoDto.getRegisteredQuantity());
    setActivate(modifyProductInfoDto.getIsActivate());
  }

  public void modifyProductByModifySeller(SellerInfoDto sellerInfoDto) {
    setStoreName(sellerInfoDto.getStoreName());
    setStoreImageUrl(sellerInfoDto.getStoreImageUrl());
  }

  @PreUpdate
  public void beforeAnyUpdate() {
    this.setUpdatedAt(LocalDateTime.now());
  }
}
