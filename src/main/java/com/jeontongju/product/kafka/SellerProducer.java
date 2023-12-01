package com.jeontongju.product.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SellerProducer<T> {

  private final KafkaTemplate<String, T> kafkaTemplate;

  public void sendCreateProduct(T createProductRecode) {
    kafkaTemplate.send("create-product", createProductRecode);
  }
}
