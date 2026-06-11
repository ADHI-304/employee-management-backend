package com.employee.management.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.employee.management.dto.ProductRequest;
import com.employee.management.dto.ProductResponse;
import com.employee.management.model.Product;
import com.employee.management.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse addProduct(ProductRequest productRequest){
        Product product = new Product();
        updateProductFromRequest(product, productRequest);
        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }

    private ProductResponse mapToProductResponse(Product product){
        ProductResponse response = new ProductResponse();
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setDescription(product.getDescription());
        response.setCategory(product.getCategory());
        response.setStockQuantity(product.getStockQuantity());
        response.setImageUrl(product.getImageUrl());
        response.setActive(product.getActive());
        return response;
    }
    private void updateProductFromRequest(Product product, ProductRequest productRequest){
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setCategory(productRequest.getCategory());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setImageUrl(productRequest.getImageUrl());
        product.setActive(productRequest.getActive());    
    }
    public List <ProductResponse> getAllProducts(){
        return productRepository.findAll().stream()
        .map(this::mapToProductResponse)
        .collect(Collectors.toList());
    }
    public ProductResponse getProductById(Long id){
        Product product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product not found with id:" + id));
        return mapToProductResponse(product);
    }
    public Optional <ProductResponse> updateProduct(Long id, ProductRequest productRequest){
        return productRepository.findById(id)
        .map(existingProduct -> {
            updateProductFromRequest(existingProduct, productRequest);
            Product savedProduct = productRepository.save(existingProduct);
            return mapToProductResponse(savedProduct);
        });
    }
    public String deleteProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found with id:" + id));
        product.setActive(false);
        productRepository.save(product);
        return "Product Deactivated";
    }
    public List <ProductResponse> searchProducts(String keyword){
        return productRepository.searchProducts(keyword).stream()
        .map(this::mapToProductResponse)
        .collect(Collectors.toList());
    }
}
