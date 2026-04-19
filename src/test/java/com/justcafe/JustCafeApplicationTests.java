package com.justcafe;

import com.justcafe.model.MenuItem;
import com.justcafe.model.CafeLocation;
import com.justcafe.repository.MenuItemRepository;
import com.justcafe.repository.CafeLocationRepository;
import com.justcafe.service.MenuService;
import com.justcafe.service.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class JustCafeApplicationTests {

    @Autowired
    private MenuService menuService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private CafeLocationRepository cafeLocationRepository;

    @Test
    void contextLoads() {
        // Application context should load without errors
    }

    @Test
    void testMenuItemsSeeded() {
        List<MenuItem> drinks = menuService.getDrinks();
        assertThat(drinks).isNotEmpty();
        assertThat(drinks.size()).isGreaterThanOrEqualTo(10);
    }

    @Test
    void testFoodsSeeded() {
        List<MenuItem> foods = menuService.getFoods();
        assertThat(foods).isNotEmpty();
        assertThat(foods.size()).isGreaterThanOrEqualTo(6);
    }

    @Test
    void testLocationsSeeded() {
        List<CafeLocation> locations = locationService.getAllLocations();
        assertThat(locations).isNotEmpty();
        assertThat(locations.size()).isGreaterThanOrEqualTo(4);
    }

    @Test
    void testDrinkCustomizationsAvailable() {
        var customizations = menuService.getCustomizationsForDrink();
        assertThat(customizations).isNotEmpty();
    }

    @Test
    void testFoodCustomizationsAvailable() {
        var customizations = menuService.getCustomizationsForFood();
        assertThat(customizations).isNotEmpty();
    }

    @Test
    void testMenuItemHasRequiredFields() {
        List<MenuItem> all = menuService.getAllAvailableItems();
        all.forEach(item -> {
            assertThat(item.getName()).isNotBlank();
            assertThat(item.getPrice()).isGreaterThan(0);
            assertThat(item.getCategory()).isIn("DRINK", "FOOD");
        });
    }

    @Test
    void testLocationHasRequiredFields() {
        List<CafeLocation> locs = locationService.getAllLocations();
        locs.forEach(loc -> {
            assertThat(loc.getBranchName()).isNotBlank();
            assertThat(loc.getAddress()).isNotBlank();
            assertThat(loc.getOpeningHours()).isNotBlank();
        });
    }
}
