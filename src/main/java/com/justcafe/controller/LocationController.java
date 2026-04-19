package com.justcafe.controller;

import com.justcafe.model.CafeLocation;
import com.justcafe.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    public ResponseEntity<List<CafeLocation>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CafeLocation> getLocation(@PathVariable Long id) {
        CafeLocation loc = locationService.getLocationById(id);
        if (loc == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(loc);
    }
}
