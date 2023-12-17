package com.jeontongju.product.kafka;

import io.github.bitbox.bitbox.util.KafkaTopicNameInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductProducer<T> {

  private final KafkaTemplate<String, T> kafkaTemplate;

  public void sendCreateProductToSearch(T createProductRecode) {
    kafkaTemplate.send(KafkaTopicNameInfo.CREATE_PRODUCT_TO_SEARCH, createProductRecode);
  }

  public void sendDeleteProductToSearch(T productId) {
    kafkaTemplate.send(KafkaTopicNameInfo.DELETE_PRODUCT_TO_SEARCH, productId);
  }

  public void sendDeleteProductToWish(T productId) {
    kafkaTemplate.send(KafkaTopicNameInfo.DELETE_PRODUCT_TO_WISH_CART, productId);
  }

  public void sendDeleteProductToReview(T productId) {
    kafkaTemplate.send(KafkaTopicNameInfo.DELETE_PRODUCT_TO_REVIEW, productId);
  }

  public void sendUpdateProductToSearch(T updateProductRecode) {
    kafkaTemplate.send(KafkaTopicNameInfo.UPDATE_PRODUCT_TO_SEARCH, updateProductRecode);
  }

  public void sendUpdateProductToReview(T updateProductThumbnailImageUrl) {
    if (updateProductThumbnailImageUrl != null) {
      kafkaTemplate.send(KafkaTopicNameInfo.UPDATE_PRODUCT_TO_REVIEW, updateProductThumbnailImageUrl);
    }

  }

  // create order - 상품에서 주문으로 재고 성공으로 보내기
  public void createOrderToOrder(T orderInfoDto) {
    kafkaTemplate.send(KafkaTopicNameInfo.CREATE_ORDER, orderInfoDto);
  }

  // 롤백 터져서 주문 취소로, 상품에서 쿠폰-포인트 세 개 중에 하나로 보내기
  public void rollbackCouponByCancel(T orderInfoDto) {
    kafkaTemplate.send(KafkaTopicNameInfo.ROLLBACK_COUPON, orderInfoDto);
  }

  public void addPointByCancel(T orderInfoDto) {
    kafkaTemplate.send(KafkaTopicNameInfo.ADD_STOCK, orderInfoDto);
  }




}
