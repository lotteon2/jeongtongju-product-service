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

  @Id private ProductRecordId productRecodeId;

  @DynamoDBAttribute(attributeName = "product")
  @DynamoDBTypeConverted(converter = DynamoDBConfig.ProductRecodeConverter.class)
  private ProductRecordContents productRecode;

  @DynamoDBAttribute(attributeName = "product_additional")
  @DynamoDBTypeConverted(converter = DynamoDBConfig.ProductRecodeAdditionalConverter.class)
  private ProductProductRecordAdditionalContents productRecodeAdditionalContents;

  @DynamoDBAttribute(attributeName = "action")
  private String action;

  @DynamoDBAttribute(attributeName = "review_count")
  private Long reviewCount;

  @DynamoDBAttribute(attributeName = "total_sales_count")
  private Long totalSalesCount;

  @DynamoDBHashKey(attributeName = "product_id")
  public String getProductId() {
    return productRecodeId != null ? productRecodeId.getProductId() : null;
  }

  public void setProductId(String productId) {
    if (productRecodeId == null) {
      productRecodeId = new ProductRecordId();
    }
    productRecodeId.setProductId(productId);
  }

  @DynamoDBHashKey(attributeName = "created_at")
  public String getCreatedAt() {
    return productRecodeId != null ? productRecodeId.getCreatedAt() : null;
  }

  public void setCreatedAt(String createdAt) {
    if (productRecodeId == null) {
      productRecodeId = new ProductRecordId();
    }
    productRecodeId.setCreatedAt(createdAt);
  }
}
