package com.jeontongju.product.domain;

import com.jeontongju.product.domain.common.BaseEntity;

import javax.persistence.*;

import com.jeontongju.product.enums.ShortsTypeEnum;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
@Table(name = "shorts")
public class Shorts extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shorts_id")
    private Long shortsId;

    @Column(name = "seller_id", unique = true, nullable = false)
    private Long sellerId;

    @Column(name = "product_id", columnDefinition = "VARCHAR(36)")
//    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "video", nullable = false)
    private String video;

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
    @Column(name = "hits", nullable = false)
    private Long hits = 0L;

    @Builder.Default
    @Column(name = "is_activate", nullable = false)
    private Boolean isActivate = true;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

}
