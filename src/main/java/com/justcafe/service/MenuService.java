package com.justcafe.service;

import com.justcafe.model.MenuItem;
import com.justcafe.model.CustomizationOption;
import com.justcafe.repository.MenuItemRepository;
import com.justcafe.repository.CustomizationOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Arrays;

@Service
public class MenuService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private CustomizationOptionRepository customizationOptionRepository;

    public List<MenuItem> getAllAvailableItems() {
        return menuItemRepository.findByAvailable(true);
    }

    public List<MenuItem> getDrinks() {
        return menuItemRepository.findByCategoryAndAvailable("DRINK", true);
    }

    public List<MenuItem> getFoods() {
        return menuItemRepository.findByCategoryAndAvailable("FOOD", true);
    }

    public MenuItem getItemById(Long id) {
        return menuItemRepository.findById(id).orElse(null);
    }

    public List<CustomizationOption> getCustomizationsForDrink() {
        return customizationOptionRepository.findByApplicableToIn(Arrays.asList("DRINK", "ALL"));
    }

    public List<CustomizationOption> getCustomizationsForFood() {
        return customizationOptionRepository.findByApplicableToIn(Arrays.asList("FOOD", "ALL"));
    }

    public List<CustomizationOption> getAllCustomizations() {
        return customizationOptionRepository.findAll();
    }
}
