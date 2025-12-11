package com.rms.application.service;

import com.rms.core.composite.MenuComponent;
import com.rms.core.composite.MenuCategory;
import com.rms.core.factory.MenuItemFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Service to initialize and demonstrate the Composite Menu structure.
 */
@Component
public class MenuInitializationService implements CommandLineRunner {

    private MenuCategory rootMenu;

    // Use CommandLineRunner to run this logic once on startup
    @Override
    public void run(String... args) throws Exception {
        initializeMenu();
        System.out.println("\n\n*** DEMONSTRATING COMPOSITE PATTERN: FULL MENU TRAVERSAL ***");
        rootMenu.display();
        System.out.println("\n*** DEMONSTRATING UNIFORM TREATMENT (Leaf Price vs Composite Price) ***");

        // Treat leaf and composite uniformly:
        MenuComponent coffeeItem = rootMenu.getComponents().get(0);
        System.out.println("Price of single item (Leaf): " + coffeeItem.getName() + " is $" + coffeeItem.getPrice());

        MenuComponent dessertsCategory = rootMenu.getComponents().get(3);
        System.out.println("Price of Desserts Category (Composite sum): $" + dessertsCategory.getPrice());
    }

    private void initializeMenu() {
        // Root of the Menu
        rootMenu = new MenuCategory("RMS Main Menu");

        // 1. Simple Item (Leaf)
        rootMenu.add(MenuItemFactory.createMenuItem("Espresso", new BigDecimal("3.00")));

        // 2. Main Dishes Category (Composite)
        MenuCategory mainDishes = new MenuCategory("Main Dishes");
        mainDishes.add(MenuItemFactory.createMenuItem("Grilled Steak", new BigDecimal("35.00"))); // Will be Stocked Out
        mainDishes.add(MenuItemFactory.createMenuItem("Pasta Carbonara", new BigDecimal("22.50")));
        rootMenu.add(mainDishes);

        // 3. Beverages Category (Composite)
        MenuCategory beverages = new MenuCategory("Beverages");
        MenuCategory coldDrinks = new MenuCategory("Cold Drinks"); // Nested Composite
        coldDrinks.add(MenuItemFactory.createMenuItem("Coke", new BigDecimal("4.00")));
        coldDrinks.add(MenuItemFactory.createMenuItem("Orange Juice", new BigDecimal("5.00")));
        beverages.add(coldDrinks);
        rootMenu.add(beverages);

        // 4. Desserts Category (Composite)
        MenuCategory desserts = new MenuCategory("Desserts");
        desserts.add(MenuItemFactory.createMenuItem("Tiramisu", new BigDecimal("10.00")));
        desserts.add(MenuItemFactory.createMenuItem("Cheesecake", new BigDecimal("9.50")));
        rootMenu.add(desserts);
    }
}