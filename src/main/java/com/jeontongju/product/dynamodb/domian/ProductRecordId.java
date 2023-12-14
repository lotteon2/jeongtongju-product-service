package com.jeontongju.product.dynamodb.domian;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTyped(DynamoDBAttributeType.M)
public class ProductRecordId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String productId;
    private String createdAt;

    @DynamoDBHashKey(attributeName = "product_id")
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @DynamoDBHashKey(attributeName = "created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
