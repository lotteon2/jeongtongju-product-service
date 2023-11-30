package com.jeontongju.product.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeontongju.product.dynamodb.domian.ProductRecodeContents;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerProducer {

  private final ObjectMapper mapper;
  private final KafkaTemplate<String, String> kafkaTemplate;

  public void sendCreateProduct(ProductRecodeContents createProductRecode) {

    try {
      kafkaTemplate.send("create-product", mapper.writeValueAsString(createProductRecode));
    } catch (JsonProcessingException e) {
      throw new RuntimeException();
    }
  }
}
