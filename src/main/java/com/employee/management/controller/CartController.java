package com.employee.management.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.management.dto.CartItemRequest;
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

            if(cartService.addToCart(studentId, cartItemRequest)){
                return ResponseEntity.badRequest().body("Product is out of stock or student not found.");
            }
            
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
