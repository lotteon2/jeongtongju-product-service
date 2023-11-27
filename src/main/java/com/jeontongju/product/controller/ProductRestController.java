package com.jeontongju.product.controller;

import com.jeontongju.product.service.ProductService;
import com.jeontongju.product.service.ShortsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ProductRestController {

  private final ProductService productService;
}
