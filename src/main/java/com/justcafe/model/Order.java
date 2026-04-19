package com.justcafe.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    private String itemName;

    private Integer quantity;

    private Double totalPrice;

    @Column(length = 1000)
    private String customizations;

    private String specialInstructions;

    private LocalDateTime orderTime;

    private String status;

    public Order(Long id, String customerName, String itemName, Integer quantity,
                 Double totalPrice, String customizations, String specialInstructions,
                 LocalDateTime orderTime, String status) {
        this.id = id;
        this.customerName = customerName;
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.customizations = customizations;
        this.specialInstructions = specialInstructions;
        this.orderTime = (orderTime != null) ? orderTime : LocalDateTime.now();
        this.status = (status != null) ? status : "PENDING";
    }
}