package com.jeontongju.product.dynamodb.repository;

import com.jeontongju.product.dynamodb.domian.ProductRecord;
import com.jeontongju.product.dynamodb.domian.ProductRecordId;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface ProductRecordRepository extends CrudRepository<ProductRecord, ProductRecordId> {
    List<ProductRecord> findByProductId(String productId);
}
