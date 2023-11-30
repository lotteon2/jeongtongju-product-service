package com.jeontongju.product.dynamodb.repository;

import com.jeontongju.product.dynamodb.domian.ProductRecode;
import com.jeontongju.product.dynamodb.domian.ProductRecodeId;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface ProductRecodeRepository extends CrudRepository<ProductRecode, ProductRecodeId> {
    List<ProductRecode> findByProductId(String productId);
}
