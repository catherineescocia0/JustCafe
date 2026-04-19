package com.justcafe.controller;

import com.justcafe.model.MenuItem;
import com.justcafe.model.CustomizationOption;
import com.justcafe.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/drinks")
    public ResponseEntity<List<MenuItem>> getDrinks() {
        return ResponseEntity.ok(menuService.getDrinks());
    }

    @GetMapping("/foods")
    public ResponseEntity<List<MenuItem>> getFoods() {
        return ResponseEntity.ok(menuService.getFoods());
    }

    @GetMapping("/all")
    public ResponseEntity<List<MenuItem>> getAllItems() {
        return ResponseEntity.ok(menuService.getAllAvailableItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getItemById(@PathVariable Long id) {
        MenuItem item = menuService.getItemById(id);
        if (item == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(item);
    }

    @GetMapping("/customizations/drink")
    public ResponseEntity<Map<String, List<CustomizationOption>>> getDrinkCustomizations() {
        List<CustomizationOption> options = menuService.getCustomizationsForDrink();
        Map<String, List<CustomizationOption>> grouped = options.stream()
            .collect(Collectors.groupingBy(CustomizationOption::getGroupName));
        return ResponseEntity.ok(grouped);
    }

    @GetMapping("/customizations/food")
    public ResponseEntity<Map<String, List<CustomizationOption>>> getFoodCustomizations() {
        List<CustomizationOption> options = menuService.getCustomizationsForFood();
        Map<String, List<CustomizationOption>> grouped = options.stream()
            .collect(Collectors.groupingBy(CustomizationOption::getGroupName));
        return ResponseEntity.ok(grouped);
    }
}
