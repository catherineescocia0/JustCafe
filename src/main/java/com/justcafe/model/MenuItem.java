package com.justcafe.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu_items")
@Data
@NoArgsConstructor
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String category; // DRINK, FOOD

    private String subCategory; // e.g., HOT, COLD, PASTRY, MEAL

    private String imageEmoji;

    private Boolean available = true;

    private Boolean customizable = true;

    public MenuItem(Long id, String name, String description, Double price,
                    String category, String subCategory, String imageEmoji,
                    Boolean available, Boolean customizable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.subCategory = subCategory;
        this.imageEmoji = imageEmoji;
        this.available = available;
        this.customizable = customizable;
    }
}