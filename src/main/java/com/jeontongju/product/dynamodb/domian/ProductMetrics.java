package com.jeontongju.product.dynamodb.domian;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;
import org.springframework.data.annotation.Id;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "product_metrics_record")
public class ProductMetrics {

  @Id
  private ProductMetricsId productMetricsId;

  @DynamoDBAttribute(attributeName = "seller_id")
  private Long sellerId;

  @DynamoDBAttribute(attributeName = "action")
  private String action;

  @DynamoDBAttribute(attributeName = "review_count")
  private Long reviewCount;

  @DynamoDBAttribute(attributeName = "total_sales_count")
  private Long totalSalesCount;

  @DynamoDBAttribute(attributeName = "total_sales_price")
  private Long totalSalesPrice;

  @DynamoDBHashKey(attributeName = "product_id")
  public String getProductId() {
    return productMetricsId != null ? productMetricsId.getProductId() : null;
  }

  public void setProductId(String productId) {
    if (productMetricsId == null) {
      productMetricsId = new ProductMetricsId();
    }
    productMetricsId.setProductId(productId);
  }

  @DynamoDBHashKey(attributeName = "created_at")
  public String getCreatedAt() {
    return productMetricsId != null ? productMetricsId.getCreatedAt() : null;
  }

  public void setCreatedAt(String createdAt) {
    if (productMetricsId == null) {
      productMetricsId = new ProductMetricsId();
    }
    productMetricsId.setCreatedAt(createdAt);
  }
}
