package com.rms.core.composite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * The Composite: Represents the containers (Categories/Sub-categories).
 */
@Data
public class MenuCategory implements MenuComponent {

    private String name;
    private final List<MenuComponent> components = new ArrayList<>();

    public MenuCategory(String name) {
        this.name = name;
    }

    @Override
    public void add(MenuComponent component) {
        components.add(component);
    }

    @Override
    public void remove(MenuComponent component) {
        components.remove(component);
    }

    // Calculating the price for a Category might not make sense, but required by interface
    @Override
    public BigDecimal getPrice() {
        // For a Category, we return the sum of all components' prices (useful for a 'meal deal' category)
        return components.stream()
                .map(MenuComponent::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void display() {
        System.out.println("\n--- Category: " + name + " ---");
        for (MenuComponent component : components) {
            component.display();
        }
    }
}