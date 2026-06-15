package com.employee.management.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.employee.management.dto.OrderItemDTO;
import com.employee.management.dto.OrderResponse;
import com.employee.management.model.CartItem;
import com.employee.management.model.Order;
import com.employee.management.model.OrderItem;
import com.employee.management.model.OrderStatus;
import com.employee.management.model.Student;
import com.employee.management.repository.CartRepository;
import com.employee.management.repository.OrderRepository;
import com.employee.management.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final StudentRepository studentRepository;
    private final CartRepository cartRepository; 
    private final OrderRepository orderRepository;

    @Transactional
    public Optional<OrderResponse> createOrder(Long studentId) {
        
        // 1. Validate the Student
        Optional<Student> studentOptional = studentRepository.findById(studentId);

        if(studentOptional.isEmpty()){
            return Optional.empty();
        }

        Student student = studentOptional.get();

        // 2. Fetch the Cart Items directly from DB
        List<CartItem> cartItems = cartRepository.findByStudent(student);
        
        if(cartItems.isEmpty()){
            return Optional.empty();
        }

        // 3. Calculate totalPrice
        BigDecimal totalPrice = cartItems.stream()
            .map(CartItem::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. Create the main Order entity
        Order order = new Order();
        order.setStudent(student);
        order.setTotalAmount(totalPrice);
        order.setStatus(OrderStatus.PENDING);

        // 5. Convert CartItems to OrderItems
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            
            // Assuming cartItem.getPrice() is the total for that quantity.
            // Unit price can be calculated as total / quantity.
            BigDecimal unitPrice = cartItem.getPrice().divide(BigDecimal.valueOf(cartItem.getQuantity()));
            orderItem.setPrice(unitPrice);
            
            return orderItem;
        }).collect(Collectors.toList());

        order.setItems(orderItems); // The cascade config in Order.java will save these automatically

        // 6. Save the Order to the database
        Order savedOrder = orderRepository.save(order);

        // 7. Clear the user's cart now that checkout is complete!
        cartRepository.deleteByStudent(student); 

        // 8. Map to Response DTO
        List<OrderItemDTO> itemDTOs = savedOrder.getItems().stream()
            .map(item -> new OrderItemDTO(
                item.getId(),
                item.getProduct().getId(),
                item.getQuantity(),
                item.getPrice(), // Unit Price
                item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())) // Subtotal
            ))
            .collect(Collectors.toList());

        return Optional.of(new OrderResponse(
            savedOrder.getId(),
            savedOrder.getTotalAmount(),
            savedOrder.getStatus(),
            itemDTOs
        ));
    }
}