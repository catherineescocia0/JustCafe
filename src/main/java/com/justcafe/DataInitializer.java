package com.justcafe;

import com.justcafe.model.*;
import com.justcafe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private MenuItemRepository menuItemRepository;
    @Autowired private CustomizationOptionRepository customizationOptionRepository;
    @Autowired private CafeLocationRepository cafeLocationRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private InventoryItemRepository inventoryItemRepository;
    @Autowired private com.justcafe.repository.SecretCodeRepository secretCodeRepository;

    @Override
    public void run(String... args) throws Exception {
        seedMenuItems();
        seedCustomizationOptions();
        seedLocations();
        seedSampleOrders();
        seedInventory();
        seedSecretCodes();
    }

    private void seedSecretCodes() {
        secretCodeRepository.save(new com.justcafe.model.SecretCode(
                null,
                "JxC.est1011.2020",
                "Underground Bar — Members Only Access",
                true
        ));
    }

    private void seedMenuItems() {
        // === HOT DRINKS ===
        menuItemRepository.save(new MenuItem(null, "Signature Espresso", "A bold, rich single shot pulled to perfection with our house blend.", 3.50, "DRINK", "HOT", "☕", true, false));
        menuItemRepository.save(new MenuItem(null, "Just Latte", "Velvety steamed milk over a double espresso shot. Our most-loved classic.", 5.50, "DRINK", "HOT", "🥛", true, true));
        menuItemRepository.save(new MenuItem(null, "Caramel Macchiato", "Espresso with vanilla-flavored syrup, milk, and rich caramel drizzle.", 6.00, "DRINK", "HOT", "🍮", true, true));
        menuItemRepository.save(new MenuItem(null, "Flat White", "Micro-foamed steamed milk over a ristretto shot. Smooth and strong.", 5.00, "DRINK", "HOT", "☕", true, true));
        menuItemRepository.save(new MenuItem(null, "Mocha", "Dark chocolate meets espresso with steamed milk and whipped cream.", 6.50, "DRINK", "HOT", "🍫", true, true));
        menuItemRepository.save(new MenuItem(null, "Dirty Matcha", "Premium ceremonial matcha with an espresso shot through the middle.", 6.50, "DRINK", "HOT", "🍵", true, true));
        menuItemRepository.save(new MenuItem(null, "Honey Oat Latte", "House espresso, oat milk, and a generous drizzle of local honey.", 6.00, "DRINK", "HOT", "🍯", true, true));
        menuItemRepository.save(new MenuItem(null, "Chai Latte", "Spiced masala chai concentrate with steamed milk. Warming and aromatic.", 5.50, "DRINK", "HOT", "🌿", true, true));

        // === COLD DRINKS ===
        menuItemRepository.save(new MenuItem(null, "Cold Brew", "Steeped 18 hours for a silky, low-acid coffee experience. Served over ice.", 5.50, "DRINK", "COLD", "🧊", true, true));
        menuItemRepository.save(new MenuItem(null, "Iced Caramel Latte", "Espresso, milk, caramel syrup, shaken and poured over ice.", 6.00, "DRINK", "COLD", "🧋", true, true));
        menuItemRepository.save(new MenuItem(null, "Iced Matcha Latte", "Ceremonial grade matcha whisked with your choice of milk over crushed ice.", 6.00, "DRINK", "COLD", "🍵", true, true));
        menuItemRepository.save(new MenuItem(null, "Nitro Cold Brew", "Cold brew infused with nitrogen for a creamy, cascading pour.", 6.50, "DRINK", "COLD", "🫧", true, false));
        menuItemRepository.save(new MenuItem(null, "Iced Brown Sugar Oat Latte", "Espresso shaken with brown sugar syrup and cinnamon over oat milk and ice.", 6.50, "DRINK", "COLD", "🤎", true, true));
        menuItemRepository.save(new MenuItem(null, "Strawberry Refresher", "Hibiscus-strawberry blend with coconut milk and real strawberry pieces.", 5.50, "DRINK", "COLD", "🍓", true, true));
        menuItemRepository.save(new MenuItem(null, "Mango Passion Fruit Cooler", "Tropical mango and passion fruit with sparkling water. Caffeine-free.", 5.00, "DRINK", "COLD", "🥭", true, false));

        // === FOOD - PASTRIES ===
        menuItemRepository.save(new MenuItem(null, "Butter Croissant", "Freshly baked, golden, laminated dough. Flaky outside, pillowy inside.", 3.50, "FOOD", "PASTRY", "🥐", true, false));
        menuItemRepository.save(new MenuItem(null, "Almond Croissant", "Classic croissant filled with rich frangipane and topped with sliced almonds.", 4.50, "FOOD", "PASTRY", "🥐", true, false));
        menuItemRepository.save(new MenuItem(null, "Chocolate Kouign-Amann", "Caramelized pastry dough folded with dark chocolate. Crispy, buttery, indulgent.", 4.50, "FOOD", "PASTRY", "🍫", true, false));
        menuItemRepository.save(new MenuItem(null, "Cinnamon Roll", "Soft, pillowy roll with cinnamon sugar swirl and cream cheese glaze.", 5.00, "FOOD", "PASTRY", "🌀", true, true));
        menuItemRepository.save(new MenuItem(null, "Blueberry Scone", "Tender buttermilk scone loaded with fresh blueberries and lemon zest glaze.", 4.00, "FOOD", "PASTRY", "🫐", true, false));
        menuItemRepository.save(new MenuItem(null, "Matcha Muffin", "Earthy matcha muffin with white chocolate chips and a sugar-crusted top.", 4.00, "FOOD", "PASTRY", "🍵", true, false));

        // === FOOD - MEALS ===
        menuItemRepository.save(new MenuItem(null, "Avocado Toast", "Sourdough toast, smashed avocado, everything bagel seasoning, poached egg.", 9.50, "FOOD", "MEAL", "🥑", true, true));
        menuItemRepository.save(new MenuItem(null, "Smoked Salmon Bagel", "Toasted everything bagel, cream cheese, smoked salmon, capers, red onion.", 11.00, "FOOD", "MEAL", "🐟", true, true));
        menuItemRepository.save(new MenuItem(null, "Egg & Cheese Sandwich", "Fluffy scrambled eggs, aged cheddar, and chives on a toasted brioche bun.", 8.50, "FOOD", "MEAL", "🥚", true, true));
        menuItemRepository.save(new MenuItem(null, "Granola Parfait", "House-made granola, Greek yogurt, seasonal fruit, and a drizzle of honey.", 7.50, "FOOD", "MEAL", "🍓", true, true));
        menuItemRepository.save(new MenuItem(null, "Caprese Panini", "Fresh mozzarella, heirloom tomatoes, basil pesto, balsamic glaze. Pressed hot.", 10.00, "FOOD", "MEAL", "🧀", true, true));
        menuItemRepository.save(new MenuItem(null, "Mushroom Gruyère Toast", "Sautéed wild mushrooms, Gruyère, fresh thyme on thick-cut sourdough.", 9.00, "FOOD", "MEAL", "🍄", true, true));
    }

    private void seedCustomizationOptions() {
        // === SIZE ===
        customizationOptionRepository.save(new CustomizationOption(null, "Size", "Small (8oz)", 0.0, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Size", "Medium (12oz)", 0.0, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Size", "Large (16oz)", 1.00, "DRINK"));

        // === MILK ===
        customizationOptionRepository.save(new CustomizationOption(null, "Milk", "Whole Milk", 0.0, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Milk", "Skim Milk", 0.0, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Milk", "Oat Milk", 0.75, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Milk", "Almond Milk", 0.75, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Milk", "Soy Milk", 0.75, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Milk", "Coconut Milk", 0.75, "DRINK"));

        // === ESPRESSO SHOTS ===
        customizationOptionRepository.save(new CustomizationOption(null, "Espresso", "Single Shot", 0.0, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Espresso", "Double Shot", 0.0, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Espresso", "Triple Shot", 0.75, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Espresso", "Decaf", 0.0, "DRINK"));

        // === SWEETNESS ===
        customizationOptionRepository.save(new CustomizationOption(null, "Sweetness", "No Sugar", 0.0, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Sweetness", "Less Sweet", 0.0, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Sweetness", "Normal Sweet", 0.0, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Sweetness", "Extra Sweet", 0.0, "DRINK"));

        // === SYRUP ADD-ONS ===
        customizationOptionRepository.save(new CustomizationOption(null, "Syrup", "Vanilla Syrup", 0.75, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Syrup", "Caramel Syrup", 0.75, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Syrup", "Hazelnut Syrup", 0.75, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Syrup", "Brown Sugar Syrup", 0.75, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Syrup", "Lavender Syrup", 0.75, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Syrup", "No Syrup", 0.0, "DRINK"));

        // === DRINK EXTRAS ===
        customizationOptionRepository.save(new CustomizationOption(null, "Extras", "Whipped Cream", 0.50, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Extras", "Extra Foam", 0.0, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Extras", "Cold Foam", 0.75, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Extras", "Cinnamon Dusting", 0.0, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Extras", "Cocoa Dusting", 0.0, "DRINK"));

        // === TEMPERATURE ===
        customizationOptionRepository.save(new CustomizationOption(null, "Temperature", "Hot", 0.0, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Temperature", "Iced", 0.0, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Temperature", "Extra Hot", 0.0, "DRINK"));
        customizationOptionRepository.save(new CustomizationOption(null, "Temperature", "Warm (not hot)", 0.0, "DRINK"));

        // === FOOD CUSTOMIZATIONS ===
        customizationOptionRepository.save(new CustomizationOption(null, "Bread", "Sourdough", 0.0, "FOOD"));
        customizationOptionRepository.save(new CustomizationOption(null, "Bread", "Whole Wheat", 0.0, "FOOD"));
        customizationOptionRepository.save(new CustomizationOption(null, "Bread", "Brioche Bun", 0.50, "FOOD"));
        customizationOptionRepository.save(new CustomizationOption(null, "Bread", "Gluten-Free Bread", 1.00, "FOOD"));

        customizationOptionRepository.save(new CustomizationOption(null, "Add-ons", "Add Extra Egg", 1.50, "FOOD"));
        customizationOptionRepository.save(new CustomizationOption(null, "Add-ons", "Add Avocado", 1.50, "FOOD"));
        customizationOptionRepository.save(new CustomizationOption(null, "Add-ons", "Add Smoked Salmon", 2.50, "FOOD"));
        customizationOptionRepository.save(new CustomizationOption(null, "Add-ons", "Add Bacon", 2.00, "FOOD"));
        customizationOptionRepository.save(new CustomizationOption(null, "Add-ons", "Add Cheese", 1.00, "FOOD"));

        customizationOptionRepository.save(new CustomizationOption(null, "Dietary", "Vegan Option", 0.0, "FOOD"));
        customizationOptionRepository.save(new CustomizationOption(null, "Dietary", "No Nuts", 0.0, "FOOD"));
        customizationOptionRepository.save(new CustomizationOption(null, "Dietary", "No Dairy", 0.0, "FOOD"));
        customizationOptionRepository.save(new CustomizationOption(null, "Dietary", "No Gluten", 1.00, "FOOD"));
    }

    private void seedLocations() {

        cafeLocationRepository.save(new CafeLocation(null,
                "Just Cafe — Makati",
                "G/F Greenbelt 3, Ayala Center, Makati Ave, Makati",
                "Makati", "Mon–Sun: 7:00 AM – 11:00 PM",
                "+63 2 8234 5678",
                "https://maps.google.com/?q=Greenbelt+3+Makati",
                14.5535, 121.0244));

        cafeLocationRepository.save(new CafeLocation(null,
                "Just Cafe — La Union",
                "San Juan Beach, La Union",
                "La Union", "Mon–Sun: 8:00 AM – 10:00 PM",
                "+63 2 8345 6789",
                "https://maps.google.com/?q=San+Juan+Beach+La Union",
                16.658397, 120.321131));

        cafeLocationRepository.save(new CafeLocation(null,
                "Just Cafe — Binondo",
                "ChinaTown Mall, Binondo, Manila",
                "Manila", "Mon–Sun: 7:30 AM – 10:00 PM",
                "+63 2 8456 7890",
                "https://maps.google.com/?q=Lucky+Chinatown+Mall+Binondo",
                14.6032191, 120.9735632));

        cafeLocationRepository.save(new CafeLocation(null,
                "Just Cafe — Las Pinas",
                "SM Southmall Las Pinas",
                "Las Pinas City", "Mon–Sun: 8:00 AM – 9:00 PM",
                "+63 2 8567 8901",
                "https://maps.google.com/?q=SM+Southmall+Las+Pinas",
                14.4324991, 121.0100751));

        cafeLocationRepository.save(new CafeLocation(null,
                "Just Cafe — Alabang",
                "G/F Alabang Town Center, Alabang-Zapote Road, Muntinlupa",
                "Muntinlupa", "Mon–Sun: 7:00 AM – 10:00 PM",
                "+63 2 8678 9012",
                "https://maps.google.com/?q=Alabang+Town+Center+Muntinlupa",
                14.4156, 121.0370));

        cafeLocationRepository.save(new CafeLocation(null,
                "Just Cafe — BGC",
                "Ground Floor, High Street South Corporate Plaza, 26th St, Bonifacio Global City",
                "Taguig", "Mon–Sun: 7:00 AM – 10:00 PM",
                "+63 2 8123 4567",
                "https://maps.google.com/?q=BGC+High+Street+Taguig",
                14.5504, 121.0534));
    }

    private void seedSampleOrders() {
        orderRepository.save(new Order(null, "Maria Santos", "Just Latte", 2, 11.00,
                "Size: Large, Milk: Oat Milk, Sweetness: Less Sweet", "Extra hot please", null, "CONFIRMED"));
        orderRepository.save(new Order(null, "Juan dela Cruz", "Cold Brew", 1, 5.50,
                "Size: Medium, Extras: Cold Foam", null, null, "READY"));
        orderRepository.save(new Order(null, "Ana Reyes", "Avocado Toast", 1, 9.50,
                "Bread: Sourdough, Add-ons: Add Extra Egg", "No red onions", null, "PENDING"));
        orderRepository.save(new Order(null, "Marco Villanueva", "Caramel Macchiato", 1, 6.75,
                "Size: Large, Milk: Almond Milk, Syrup: Extra Caramel", null, null, "CONFIRMED"));
        orderRepository.save(new Order(null, "Lena Cruz", "Cinnamon Roll", 2, 10.00,
                "", "Slightly warmed please", null, "READY"));
    }

    private void seedInventory() {
        // === DRINKS (servings/cups available) ===
        inventoryItemRepository.save(new InventoryItem(null, "Signature Espresso", "DRINK", 80, 15, "cups", 2.50, "House Roast PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Just Latte", "DRINK", 60, 15, "cups", 3.00, "House Roast PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Caramel Macchiato", "DRINK", 55, 10, "cups", 3.50, "House Roast PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Flat White", "DRINK", 50, 10, "cups", 2.80, "House Roast PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Mocha", "DRINK", 45, 10, "cups", 3.20, "House Roast PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Dirty Matcha", "DRINK", 40, 8, "cups", 3.50, "Matcha Source MNL", null));
        inventoryItemRepository.save(new InventoryItem(null, "Honey Oat Latte", "DRINK", 35, 8, "cups", 3.20, "House Roast PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Chai Latte", "DRINK", 30, 8, "cups", 2.50, "Spice Traders PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Cold Brew", "DRINK", 25, 10, "cups", 2.00, "House Roast PH", "Batch brewed every 18hrs"));
        inventoryItemRepository.save(new InventoryItem(null, "Iced Caramel Latte", "DRINK", 50, 10, "cups", 3.20, "House Roast PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Iced Matcha Latte", "DRINK", 30, 8, "cups", 3.50, "Matcha Source MNL", null));
        inventoryItemRepository.save(new InventoryItem(null, "Nitro Cold Brew", "DRINK", 8, 5, "cups", 2.50, "House Roast PH", "Nitrogen keg — check pressure daily"));
        inventoryItemRepository.save(new InventoryItem(null, "Iced Brown Sugar Oat Latte", "DRINK", 40, 10, "cups", 3.20, "House Roast PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Strawberry Refresher", "DRINK", 20, 8, "cups", 2.80, "Fresh Fruits MNL", null));
        inventoryItemRepository.save(new InventoryItem(null, "Mango Passion Fruit Cooler", "DRINK", 0, 8, "cups", 2.50, "Fresh Fruits MNL", "Out of stock - supplier delivery Friday"));

        // === FOOD (pieces) ===
        inventoryItemRepository.save(new InventoryItem(null, "Butter Croissant", "FOOD", 24, 5, "pieces", 1.80, "Golden Bake BGC", "Delivered fresh daily 6AM"));
        inventoryItemRepository.save(new InventoryItem(null, "Almond Croissant", "FOOD", 18, 5, "pieces", 2.20, "Golden Bake BGC", null));
        inventoryItemRepository.save(new InventoryItem(null, "Chocolate Kouign-Amann", "FOOD", 12, 4, "pieces", 2.50, "Golden Bake BGC", null));
        inventoryItemRepository.save(new InventoryItem(null, "Cinnamon Roll", "FOOD", 10, 4, "pieces", 2.00, "Golden Bake BGC", null));
        inventoryItemRepository.save(new InventoryItem(null, "Blueberry Scone", "FOOD", 3, 5, "pieces", 1.80, "Golden Bake BGC", "Running low"));
        inventoryItemRepository.save(new InventoryItem(null, "Matcha Muffin", "FOOD", 15, 5, "pieces", 1.90, "Golden Bake BGC", null));
        inventoryItemRepository.save(new InventoryItem(null, "Avocado Toast", "FOOD", 20, 5, "servings", 3.50, "Fresh Produce MNL", null));
        inventoryItemRepository.save(new InventoryItem(null, "Smoked Salmon Bagel", "FOOD", 12, 4, "servings", 5.00, "Seafood Direct PH", "Keep refrigerated"));
        inventoryItemRepository.save(new InventoryItem(null, "Egg & Cheese Sandwich", "FOOD", 25, 5, "servings", 2.50, "Fresh Produce MNL", null));
        inventoryItemRepository.save(new InventoryItem(null, "Granola Parfait", "FOOD", 18, 5, "servings", 2.20, "Local Farms PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Caprese Panini", "FOOD", 14, 4, "servings", 4.00, "Fresh Produce MNL", null));
        inventoryItemRepository.save(new InventoryItem(null, "Mushroom Gruyère Toast", "FOOD", 2, 4, "servings", 4.20, "Fresh Produce MNL", "Nearly out"));

        // === INGREDIENTS ===
        inventoryItemRepository.save(new InventoryItem(null, "Espresso Beans (House Blend)", "INGREDIENT", 12, 3, "kg", 18.00, "House Roast PH", "Roasted in small batches"));
        inventoryItemRepository.save(new InventoryItem(null, "Oat Milk", "INGREDIENT", 20, 5, "liters", 3.50, "Minor Figures PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Whole Milk", "INGREDIENT", 30, 8, "liters", 1.50, "Dairy Fresh PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Almond Milk", "INGREDIENT", 8, 4, "liters", 4.00, "Pacific Foods PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Ceremonial Matcha Powder", "INGREDIENT", 2, 2, "kg", 45.00, "Matcha Source MNL", "Order immediately when low"));
        inventoryItemRepository.save(new InventoryItem(null, "Vanilla Syrup", "INGREDIENT", 6, 2, "bottles", 8.00, "Monin PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Caramel Syrup", "INGREDIENT", 4, 2, "bottles", 8.00, "Monin PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Brown Sugar Syrup", "INGREDIENT", 5, 2, "bottles", 7.50, "Monin PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Hazelnut Syrup", "INGREDIENT", 3, 2, "bottles", 8.00, "Monin PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Lavender Syrup", "INGREDIENT", 2, 2, "bottles", 9.00, "Monin PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Coconut Milk", "INGREDIENT", 15, 4, "cans", 2.00, "Chaokoh PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Fresh Eggs", "INGREDIENT", 48, 12, "pieces", 0.30, "Local Farms PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Avocado", "INGREDIENT", 20, 6, "pieces", 1.20, "Fresh Produce MNL", null));
        inventoryItemRepository.save(new InventoryItem(null, "Sourdough Bread", "INGREDIENT", 4, 2, "loaves", 5.00, "Golden Bake BGC", null));

        // === SUPPLIES ===
        inventoryItemRepository.save(new InventoryItem(null, "Hot Cups (12oz)", "SUPPLY", 500, 100, "pieces", 0.15, "EcoPack PH", "Compostable"));
        inventoryItemRepository.save(new InventoryItem(null, "Cold Cups (16oz)", "SUPPLY", 300, 80, "pieces", 0.18, "EcoPack PH", "Compostable"));
        inventoryItemRepository.save(new InventoryItem(null, "Cup Lids", "SUPPLY", 400, 100, "pieces", 0.08, "EcoPack PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Paper Bags", "SUPPLY", 200, 50, "pieces", 0.25, "EcoPack PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Wooden Stir Sticks", "SUPPLY", 50, 20, "bundles", 1.50, "EcoPack PH", null));
        inventoryItemRepository.save(new InventoryItem(null, "Napkins", "SUPPLY", 15, 10, "packs", 2.00, "Office Warehouse", null));
    }
}