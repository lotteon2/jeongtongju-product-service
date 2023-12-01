package com.jeontongju.product.kafka;

import com.jeontongju.product.dynamodb.domian.ProductRecodeContents;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerProducer {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public void sendCreateProduct(ProductRecodeContents createProductRecode) {
    kafkaTemplate.send("create-product", createProductRecode);
  }
}
