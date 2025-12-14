package com.stockbrokerage.order.model;

import com.stockbrokerage.order.enums.OrderType;
import com.stockbrokerage.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trading_order")
@Data // Lombok: Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok: Generates no-argument constructor
@AllArgsConstructor // Lombok: Generates all-argument constructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Production-level check: Use a UUID or similar for public ID to prevent enumeration
    @Column(unique = true, nullable = false, updatable = false)
    private String orderReferenceId;

    // Relationships
    @Column(nullable = false)
    private Long accountId; // The user's account placing the order

    @Column(nullable = false)
    private String symbol; // Ticker symbol (e.g., AAPL, GOOGL)

    // Order Details
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType type;

    @Column(nullable = false)
    private BigDecimal quantity;

    @Column(nullable = false)
    private BigDecimal currentPrice; // Price at the time the order was placed/checked

    // Details specific to Limit/Stop orders
    private BigDecimal limitPrice;
    private BigDecimal stopPrice;

    // Tracking
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.NEW;

    @Column(nullable = false)
    private LocalDateTime placedAt = LocalDateTime.now();

    private LocalDateTime executedAt;

    // Helper method to generate a production-like reference ID
    @PrePersist
    public void generateReferenceId() {
        if (this.orderReferenceId == null) {
            // Placeholder: In a real system, use UUID.randomUUID().toString()
            this.orderReferenceId = "ORD-" + System.currentTimeMillis();
        }
    }
}