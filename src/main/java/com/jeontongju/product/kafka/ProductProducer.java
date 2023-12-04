package com.jeontongju.product.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductProducer<T> {

  private final KafkaTemplate<String, T> kafkaTemplate;

  public void sendCreateProductToSearch(T createProductRecode) {
    kafkaTemplate.send("create-product", createProductRecode);
  }

  public void sendDeleteProductToSearch(T productId) {
    kafkaTemplate.send("delete-product-to-search", productId);
  }

  public void sendDeleteProductToWish(T productId) {
    kafkaTemplate.send("delete-product-to-wish-cart", productId);
  }

  public void sendDeleteProductToReview(T productId) {
    kafkaTemplate.send("delete-product-to-review", productId);
  }
}
