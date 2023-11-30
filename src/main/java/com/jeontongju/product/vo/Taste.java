package com.jeontongju.product.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Taste {
    private Long sour;
    private Long sweet;
    private Long scent;
    private Long carbonation;
    private Long body;
}
