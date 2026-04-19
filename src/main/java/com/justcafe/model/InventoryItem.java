package com.justcafe.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventory_items")
@Data
@NoArgsConstructor
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String itemName;

    @Column(nullable = false)
    private String category; // DRINK, FOOD, INGREDIENT, SUPPLY

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false)
    private Integer lowStockThreshold; // warn when stock <= this

    private String unit; // cups, pieces, kg, liters, bags

    private Double costPerUnit;

    private String supplier;

    private String notes;

    public InventoryItem(Long id, String itemName, String category,
                         Integer stockQuantity, Integer lowStockThreshold,
                         String unit, Double costPerUnit, String supplier, String notes) {
        this.id = id;
        this.itemName = itemName;
        this.category = category;
        this.stockQuantity = stockQuantity;
        this.lowStockThreshold = lowStockThreshold;
        this.unit = unit;
        this.costPerUnit = costPerUnit;
        this.supplier = supplier;
        this.notes = notes;
    }

    public String getStockStatus() {
        if (stockQuantity <= 0) return "OUT_OF_STOCK";
        if (stockQuantity <= lowStockThreshold) return "LOW";
        return "OK";
    }
}