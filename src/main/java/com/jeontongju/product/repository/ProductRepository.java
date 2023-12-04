package com.jeontongju.product.repository;

import com.jeontongju.product.domain.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, String> {

  @Query("select p from Product p where p.sellerId = :sellerId AND p.isDeleted = false ")
  List<Product> findBySellerIdWithoutIsDeleted(Long sellerId);
}
