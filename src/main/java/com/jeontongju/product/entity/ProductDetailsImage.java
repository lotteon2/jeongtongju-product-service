package com.jeontongju.product.entity;

import com.jeontongju.product.entity.common.BaseEntity;
import javax.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
@Table(name = "product_details_image")
public class ProductDetailsImage extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_details_image_id")
  private Long productDetailsImageId;

  @JoinColumn(name = "product_id")
  @OneToOne(targetEntity = Product.class, fetch = FetchType.LAZY)
  private Product product;

  @Column(name = "image_url", nullable = false)
  private String imageUrl;

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
