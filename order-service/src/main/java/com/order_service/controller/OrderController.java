package com.order_service.controller;


import com.order_service.entity.Order;
import com.order_service.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final HttpServletRequest request;

    @GetMapping("/health")
    public ResponseEntity<String> health(){
        return ResponseEntity.ok("Order Service running");
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> placeOrder(@RequestParam Long bookId,@RequestParam int quantity){
        try {
            String userId = request.getHeader("X-User-Id");
            if(userId == null){
                return ResponseEntity.badRequest()
                        .body(Map.of("error","User ID header missing"));
            }
            Order order = orderService.placeOrder(bookId,quantity,userId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error",e.getMessage()));
        }
    }

    @GetMapping("/my-orders")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Order>> getMyOrders(){
        String userId = request.getHeader("X-User-Id");
        return ResponseEntity.ok(orderService.getMyOrders(userId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getOrders(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }

}
