package com.employee.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.employee.management.model.Product;

public interface ProductRepository extends JpaRepository <Product, Long>{
    @Query("""
        Select p FROM Product p
        WHERE p.active=true
        AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    List <Product> searchProducts(String keyword);
}
