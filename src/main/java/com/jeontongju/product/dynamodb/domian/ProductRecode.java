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
public class ProductRecode {

  @Id private ProductRecodeId productRecodeId;

  @DynamoDBAttribute(attributeName = "product")
  @DynamoDBTypeConverted(converter = DynamoDBConfig.ProductRecodeConverter.class)
  private ProductRecodeContents createProductRecode;

  @DynamoDBAttribute(attributeName = "action")
  private String action;

  @DynamoDBHashKey(attributeName = "product_id")
  public String getProductId() {
    return productRecodeId != null ? productRecodeId.getProductId() : null;
  }

  public void setProductId(String productId) {
    if (productRecodeId == null) {
      productRecodeId = new ProductRecodeId();
    }
    productRecodeId.setProductId(productId);
  }

  @DynamoDBHashKey(attributeName = "created_at")
  public String getCreatedAt() {
    return productRecodeId != null ? productRecodeId.getCreatedAt() : null;
  }

  public void setCreatedAt(String createdAt) {
    if (productRecodeId == null) {
      productRecodeId = new ProductRecodeId();
    }
    productRecodeId.setCreatedAt(createdAt);
  }
}
