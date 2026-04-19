package com.justcafe.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customization_options")
@Data
@NoArgsConstructor
public class CustomizationOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String groupName;

    @Column(nullable = false)
    private String optionName;

    private Double additionalPrice;

    private String applicableTo; // "DRINK", "FOOD", "ALL"

    public CustomizationOption(Long id, String groupName, String optionName,
                               Double additionalPrice, String applicableTo) {
        this.id = id;
        this.groupName = groupName;
        this.optionName = optionName;
        this.additionalPrice = additionalPrice;
        this.applicableTo = applicableTo;
    }
}