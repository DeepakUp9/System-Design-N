package com.amazon.orderservice.domain.model;

import com.amazon.orderservice.domain.builder.ConcreteOrderBuilder;
import com.amazon.orderservice.domain.state.OrderState;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Context for State Pattern & Product for Builder Pattern
@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // LLD: The Context holds the current State reference
    @Transient // Not persisted directly; the state name (string) is persisted
    private OrderState currentState;

    // Persisted field for the State
    @Column(nullable = false)
    private String status; // e.g., PENDING, PROCESSING, SHIPPED

    private Long userId;

    // Builder Pattern handles the aggregation of OrderItems
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Embedded
    private ShippingInfo shippingInfo; // Use @Embedded for address details

    private BigDecimal totalAmount;
    private LocalDateTime orderDate;

    // LLD: Private Constructor for the Builder Pattern!
    private Order() {
        this.orderDate = LocalDateTime.now();
        this.status = "PENDING"; // Default state
        // Initial state set in service/builder after loading
    }

    // In Order.java - Add this method:
    public static Order createEmpty() {
        return new Order();  // Can call private constructor from within class
    }

    // Setters for OrderState are needed for internal service logic/loading
    public void setCurrentState(OrderState currentState) {
        this.currentState = currentState;
        this.status = currentState.getStatusName(); // Update the persisted status field
    }

    // Getters Omitted for brevity

    // LLD: State Pattern Delegation Methods (The Context delegates behavior)
    public void processPayment() {
        this.currentState.processPayment(this);
    }

    public void shipOrder() {
        this.currentState.shipOrder(this);
    }

    public void cancelOrder() {
        this.currentState.cancelOrder(this);
    }

    // The Order needs a static factory method to initiate the Builder
    public static ConcreteOrderBuilder builder() {
        return new ConcreteOrderBuilder();
    }

    // Ensure all state transition logic is handled by the State objects!
}