package com.employee.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.employee.management.model.Order;

@Repository
public interface OrderRepository extends JpaRepository <Order, Long>{
    
}
