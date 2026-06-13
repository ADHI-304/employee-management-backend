package com.employee.management.dto;

import java.math.BigDecimal;

import com.employee.management.model.Product;
import com.employee.management.model.Student;

import lombok.Data;

@Data
public class CartItemRequest {

    private Integer quantity;
    private BigDecimal price;
    private Product product;
    private Student student;

}
