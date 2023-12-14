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

  public void sendUpdateProductToSearch(T updateProductRecode) {
    kafkaTemplate.send("update-product-to-search", updateProductRecode);
  }

  public void sendUpdateProductToReview(T updateProductThumbnailImageUrl) {
    if (updateProductThumbnailImageUrl != null) {
      kafkaTemplate.send("update-product-to-review", updateProductThumbnailImageUrl);
    }
  }

  // create order / 상품에서 주문으로 재고 성공으로 보내기
  public void createOrder() {

  }

  // 롤백 터져서 주문 취소로, 상품에서 쿠폰-포인트 세 개 중에 하나로 보내기
  public void rollbackOrder() {
  }




}
