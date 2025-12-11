package com.rms.core.composite;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The Leaf: Represents the simple objects in the composition (the actual dishes).
 */
@Data
@AllArgsConstructor
public class MenuItem implements MenuComponent {

    private String name;
    private BigDecimal price;
    private boolean isAvailable;

    @Override
    public void display() {
        String availability = isAvailable ? " (Available)" : " (SOLD OUT)";
        System.out.println("   - Item: " + name + " | Price: $" + price + availability);
    }
}