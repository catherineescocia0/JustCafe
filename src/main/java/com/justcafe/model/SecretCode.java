package com.justcafe.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "secret_codes")
@Data
@NoArgsConstructor
public class SecretCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    private String description;

    private Boolean active;

    public SecretCode(Long id, String code, String description, Boolean active) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.active = active;
    }
}