package com.employee.management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.management.dto.CartItemRequest;
import com.employee.management.dto.CartItemResponse;
import com.employee.management.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    
    @PostMapping
    public ResponseEntity <String> addToCart(
        @RequestHeader("X-Student-ID") Long studentId,
        @RequestBody CartItemRequest cartItemRequest){

            if (!cartService.addToCart(studentId, cartItemRequest)) {
                return ResponseEntity.badRequest()
                .body("Product is out of stock or student not found.");
            }

            return ResponseEntity.status(HttpStatus.CREATED)
            .body("Product added to cart.");
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity <Void> removeFromCart(
        @RequestHeader("X-Student-ID") Long studentId,
        @PathVariable Long productId){

        boolean deleted = cartService.deleteFromCart(studentId, productId);

        if(!deleted){
            return ResponseEntity.badRequest().build();
        }    

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity <List<CartItemResponse>> getCart(

        @RequestHeader("X-Student-ID") Long studentId) {
            return ResponseEntity.ok(cartService.getCart(studentId));
            
    }

}
