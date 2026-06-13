package com.employee.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.employee.management.model.CartItem;
import com.employee.management.model.Product;
import com.employee.management.model.Student;

@Repository
public interface CartRepository extends JpaRepository <CartItem, Long> {

    CartItem findByStudentAndProduct(Student student, Product product);

}
