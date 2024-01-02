package com.jeontongju.product.dto.response;

import com.jeontongju.product.domain.Shorts;
import com.jeontongju.product.enums.ShortsTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class GetShortsByConsumerDto {

    private Long shortsId;
    private String shortsTitle;
    private String shortsThumbnailUrl;
    private String shortsDescription;
    private String shortsPreviewUrl;
    private String shortsVideoUrl;
    private Long shortsHits;
    private String targetId;

    public static GetShortsByConsumerDto toDto(Shorts shorts) {
        String targetId = "";
        if (shorts.getType() != ShortsTypeEnum.PRODUCT
        ) {
            targetId = "product/" + shorts.getProductId();
        } else if (shorts.getType() != ShortsTypeEnum.SELLER) {
            targetId = "seller/" + shorts.getSellerId();
        }

        return GetShortsByConsumerDto.builder()
                .shortsId(shorts.getShortsId())
                .shortsTitle(shorts.getTitle())
                .shortsThumbnailUrl(shorts.getThumbnail())
                .shortsDescription(shorts.getDescription())
                .shortsPreviewUrl(shorts.getPreview())
                .shortsVideoUrl(shorts.getVideo())
                .shortsHits(shorts.getShortsHits())
                .targetId(targetId)
                .build();

    }
}
