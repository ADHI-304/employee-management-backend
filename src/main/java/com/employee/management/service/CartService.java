package com.employee.management.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.employee.management.dto.CartItemRequest;
import com.employee.management.model.CartItem;
import com.employee.management.model.Product;
import com.employee.management.model.Student;
import com.employee.management.repository.CartRepository;
import com.employee.management.repository.ProductRepository;
import com.employee.management.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final  ProductRepository productRepository;
    private final StudentRepository studentRepository;
    private final CartRepository cartRepository;
    
    public Boolean addToCart(Long studentId, CartItemRequest cartItemRequest){

        Optional <Product> productOp = productRepository.findById(cartItemRequest.getProduct().getId());

        if ((productOp.isEmpty())) {
            return false;
        }

        Product product = productOp.get();

        if(product.getStockQuantity() < cartItemRequest.getQuantity()){
            return false;
        }

        Optional <Student> studentOp = studentRepository.findById(Long.valueOf(studentId));

        if(studentOp.isEmpty()){
            return false;
        }

        Student student = studentOp.get();
        CartItem existingCartItem = cartRepository.findByStudentAndProduct(student, product);

        if(existingCartItem == null){

            CartItem cartItem = new CartItem();
            cartItem.setStudent(student);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            cartRepository.save(cartItem);

        }else{

            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartRepository.save(existingCartItem);
            
        }
        
        return true;
    }
}
