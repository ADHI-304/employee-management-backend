package com.employee.management.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.employee.management.dto.CartItemRequest;
import com.employee.management.dto.CartItemResponse;
import com.employee.management.model.CartItem;
import com.employee.management.model.Product;
import com.employee.management.model.Student;
import com.employee.management.repository.CartRepository;
import com.employee.management.repository.ProductRepository;
import com.employee.management.repository.StudentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductRepository productRepository;
    private final StudentRepository studentRepository;
    private final CartRepository cartRepository;

    public Boolean addToCart(Long studentId, CartItemRequest request) {
        Optional<Product> productOp = productRepository.findById(request.getProductId()); // ← use productId
        if (productOp.isEmpty()) return false;

        Product product = productOp.get();
        if (product.getStockQuantity() < request.getQuantity()) return false;

        Optional<Student> studentOp = studentRepository.findById(studentId);
        if (studentOp.isEmpty()) return false;

        Student student = studentOp.get();
        CartItem existingCartItem = cartRepository.findByStudentAndProduct(student, product);

        if (existingCartItem == null) {
            CartItem cartItem = new CartItem();
            cartItem.setStudent(student);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartRepository.save(cartItem);
        } else {
            int newQty = existingCartItem.getQuantity() + request.getQuantity();
            existingCartItem.setQuantity(newQty);
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(newQty)));
            cartRepository.save(existingCartItem);
        }
        return true;
    }

    @Transactional
    public boolean deleteFromCart(Long studentId, Long productId) {
        Optional<Product> productOp = productRepository.findById(productId);
        Optional<Student> studentOp = studentRepository.findById(studentId);
        if (productOp.isPresent() && studentOp.isPresent()) {
            cartRepository.deleteByStudentAndProduct(studentOp.get(), productOp.get());
            return true;
        }
        return false;
    }

    public List <CartItemResponse> getCart(Long studentId) {   // ← returns response DTOs
        Optional <Student> studentOp = studentRepository.findById(studentId);
        if(studentOp.isEmpty()){
            return Collections.emptyList();
        }
        return cartRepository.findByStudent(studentOp.get())
        .stream()
        .map(item -> new CartItemResponse(
            item.getId(),
            item.getProduct().getId(),
            item.getProduct().getName(),
            item.getQuantity(),
            item.getPrice(),
            item.getCreatedAt(),
            item.getUpdatedAt()
        ))
        .collect(Collectors.toList());
    }
}