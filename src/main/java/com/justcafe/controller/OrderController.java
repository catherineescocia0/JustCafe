package com.justcafe.controller;

import com.justcafe.model.Order;
import com.justcafe.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> placeOrder(@RequestBody Order order) {
        Order saved = orderService.placeOrder(order);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("orderId", saved.getId());
        response.put("message", "Your order has been placed! Order #" + saved.getId());
        response.put("status", saved.getStatus());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(order);
    }
}
