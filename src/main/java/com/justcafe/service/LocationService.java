package com.justcafe.service;

import com.justcafe.model.CafeLocation;
import com.justcafe.repository.CafeLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LocationService {

    @Autowired
    private CafeLocationRepository cafeLocationRepository;

    public List<CafeLocation> getAllLocations() {
        return cafeLocationRepository.findAll();
    }

    public CafeLocation getLocationById(Long id) {
        return cafeLocationRepository.findById(id).orElse(null);
    }
}
