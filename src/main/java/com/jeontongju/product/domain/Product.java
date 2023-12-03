package com.jeontongju.product.domain;

import com.jeontongju.product.domain.common.BaseEntity;
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

  @Column(name = "name", nullable = false, length = 30)
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

  @Builder.Default
  @Column(name = "total_sales_count", nullable = false)
  private Long totalSalesCount = 0L;

  @Builder.Default
  @Column(name = "review_count", nullable = false)
  private Long reviewCount = 0L;

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
}
