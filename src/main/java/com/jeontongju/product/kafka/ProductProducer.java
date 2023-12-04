package com.jeontongju.product.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductProducer<T> {

  private final KafkaTemplate<String, T> kafkaTemplate;

  public void sendCreateProduct(T createProductRecode) {
    kafkaTemplate.send("create-product", createProductRecode);
  }

  public void sendDeleteProduct(T productId) { kafkaTemplate.send("delete-product", productId);}

}
