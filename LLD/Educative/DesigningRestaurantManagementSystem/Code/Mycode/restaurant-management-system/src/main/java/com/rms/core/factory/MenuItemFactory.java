package com.rms.core.factory;

import com.rms.core.composite.MenuItem;
import com.rms.core.composite.MenuComponent;
import java.math.BigDecimal;

/**
 * The Factory: Centralizes the creation logic for Menu Items.
 */
public class MenuItemFactory {

    // Static method to create a menu item with default availability logic
    public static MenuComponent createMenuItem(String name, BigDecimal basePrice) {
        // Real-world logic would check inventory/supply chain here
        boolean available = checkInventory(name);

        System.out.println("Factory: Created item '" + name + "'. Status: " + (available ? "Available" : "Stocked Out"));
        return new MenuItem(name, basePrice, available);
    }

    // Simulating complex inventory check logic
    private static boolean checkInventory(String itemName) {
        // Edge Case: Coffee is always available; Steak is sometimes not.
        return !itemName.toLowerCase().contains("steak");
    }
}