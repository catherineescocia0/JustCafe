package com.justcafe.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cafe_locations")
@Data
@NoArgsConstructor
public class CafeLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String branchName;

    @Column(nullable = false)
    private String address;

    private String city;

    private String openingHours;

    private String phone;

    private String mapUrl;

    private Double latitude;

    private Double longitude;

    public CafeLocation(Long id, String branchName, String address, String city,
                        String openingHours, String phone, String mapUrl,
                        Double latitude, Double longitude) {
        this.id = id;
        this.branchName = branchName;
        this.address = address;
        this.city = city;
        this.openingHours = openingHours;
        this.phone = phone;
        this.mapUrl = mapUrl;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}