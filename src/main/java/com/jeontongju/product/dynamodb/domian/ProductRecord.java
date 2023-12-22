package com.jeontongju.product.dynamodb.domian;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import javax.persistence.Id;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.jeontongju.product.config.DynamoDBConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "product_recode")
public class ProductRecord {

  @Id private ProductRecordId productRecordId;

  @DynamoDBAttribute(attributeName = "product")
  @DynamoDBTypeConverted(converter = DynamoDBConfig.productRecordConverter.class)
  private ProductRecordContents productRecord;

  @DynamoDBAttribute(attributeName = "product_additional")
  @DynamoDBTypeConverted(converter = DynamoDBConfig.productRecordAdditionalConverter.class)
  private ProductRecordAdditionalContents productRecordAdditionalContents;

  @DynamoDBAttribute(attributeName = "action")
  private String action;

  @DynamoDBHashKey(attributeName = "product_id")
  public String getProductId() {
    return productRecordId != null ? productRecordId.getProductId() : null;
  }

  public void setProductId(String productId) {
    if (productRecordId == null) {
      productRecordId = new ProductRecordId();
    }
    productRecordId.setProductId(productId);
  }

  @DynamoDBHashKey(attributeName = "created_at")
  public String getCreatedAt() {
    return productRecordId != null ? productRecordId.getCreatedAt() : null;
  }

  public void setCreatedAt(String createdAt) {
    if (productRecordId == null) {
      productRecordId = new ProductRecordId();
    }
    productRecordId.setCreatedAt(createdAt);
  }
}
