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
  @DynamoDBHashKey(attributeName = "product_id")
  private String productId;

  @DynamoDBAttribute(attributeName = "review_count")
  private Long reviewCount;

  @DynamoDBAttribute(attributeName = "total_sales_count")
  private Long totalSalesCount;
}
