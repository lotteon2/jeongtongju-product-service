package com.jeontongju.product.domain;

import com.jeontongju.product.domain.common.BaseEntity;
import com.jeontongju.product.dto.request.UpdateShortsDto;
import com.jeontongju.product.enums.ShortsTypeEnum;
import javax.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
@Table(name = "shorts")
public class Shorts extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "shorts_id")
  private Long shortsId;

  @Column(name = "seller_id", nullable = false)
  private Long sellerId;

  @Column(name = "product_id", columnDefinition = "CHAR(36)")
  private String productId;

  @Column(name = "video", nullable = false)
  private String video;

  @Column(name = "preview", nullable = false)
  private String preview;

  @Column(name = "thumbnail", nullable = false)
  private String thumbnail;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description", nullable = false)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private ShortsTypeEnum type;

  @Builder.Default
  @Column(name = "shortsHits", nullable = false)
  private Long shortsHits = 0L;

  @Builder.Default
  @Column(name = "is_activate", nullable = false)
  private Boolean isActivate = true;

  @Builder.Default
  @Column(name = "is_deleted", nullable = false)
  private Boolean isDeleted = false;

  public void setTitle(String title) {
    if (title != null) {
      this.title = title;
    }
  }

  public void setDescription(String description) {
    if (description != null) {
      this.description = description;
    }
  }

  public void setShortsHits(Long shortsHits) {
    this.shortsHits = shortsHits;
  }

  public void setActivate(Boolean activate) {
    if (activate != null) {
      isActivate = activate;
    }
  }

  public void setDeleted(Boolean deleted) {
    isDeleted = deleted;
  }

  public void modifyShorts(UpdateShortsDto updateShortsDto) {
    setTitle(updateShortsDto.getShortsTitle());
    setDescription(updateShortsDto.getShortsDescription());
    setActivate(updateShortsDto.getIsActivate());
  }
}
