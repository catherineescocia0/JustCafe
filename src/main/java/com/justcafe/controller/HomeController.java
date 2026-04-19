package com.justcafe.controller;

import com.justcafe.service.MenuService;
import com.justcafe.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private LocationService locationService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("drinks", menuService.getDrinks());
        model.addAttribute("foods", menuService.getFoods());
        model.addAttribute("locations", locationService.getAllLocations());
        model.addAttribute("drinkCustomizations", menuService.getCustomizationsForDrink());
        model.addAttribute("foodCustomizations", menuService.getCustomizationsForFood());
        return "index";
    }
}
