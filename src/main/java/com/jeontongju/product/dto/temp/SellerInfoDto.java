package com.jeontongju.product.dto.temp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 domain : seller
 detail : 상품 등록 시, 들고 있을 셀러 정보
 method : feign
 comment :
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerInfoDto {

    private String storeName;
    private String storeImageUrl;

}
