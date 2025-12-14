package com.stockbrokerage.order.dto;

import com.stockbrokerage.order.enums.OrderType;
import com.stockbrokerage.order.exceptions.OrderProcessingException;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import java.math.BigDecimal;

/**
 * Builder Pattern: The Order Request DTO uses a nested Builder class
 * to ensure valid construction, especially for conditional fields like limitPrice.
 */
@Getter
public class OrderRequest {

    @NotNull(message = "Account ID is mandatory")
    private final Long accountId;

    @NotBlank(message = "Symbol is mandatory")
    private final String symbol;

    @NotNull(message = "Order type is mandatory")
    private final OrderType type;

    @DecimalMin(value = "0.01", message = "Quantity must be positive")
    private final BigDecimal quantity;

    private final BigDecimal currentPrice;
    private final BigDecimal limitPrice;
    private final BigDecimal stopPrice;

    // 1. Private Constructor ensures creation is only through the Builder
    private OrderRequest(OrderRequestBuilder builder) {
        this.accountId = builder.accountId;
        this.symbol = builder.symbol;
        this.type = builder.type;
        this.quantity = builder.quantity;
        this.currentPrice = builder.currentPrice;
        this.limitPrice = builder.limitPrice;
        this.stopPrice = builder.stopPrice;

        // Final validation logic: Production-level guard rail
        if ((type == OrderType.LIMIT || type == OrderType.STOP_LIMIT) && this.limitPrice == null) {
            throw new OrderProcessingException("Limit price is mandatory for " + type);
        }
        if ((type == OrderType.STOP_LOSS || type == OrderType.STOP_LIMIT) && this.stopPrice == null) {
            throw new OrderProcessingException("Stop price is mandatory for " + type);
        }
    }

    // 2. Static method to get the Builder instance
    public static OrderRequestBuilder builder() {
        return new OrderRequestBuilder();
    }

    // 3. The Builder Class
    public static class OrderRequestBuilder {
        private Long accountId;
        private String symbol;
        private OrderType type;
        private BigDecimal quantity;
        private BigDecimal currentPrice;
        private BigDecimal limitPrice;
        private BigDecimal stopPrice;

        public OrderRequestBuilder accountId(Long accountId) { this.accountId = accountId; return this; }
        public OrderRequestBuilder symbol(String symbol) { this.symbol = symbol; return this; }
        public OrderRequestBuilder type(OrderType type) { this.type = type; return this; }
        public OrderRequestBuilder quantity(BigDecimal quantity) { this.quantity = quantity; return this; }
        public OrderRequestBuilder currentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; return this; }

        // Optional parameters
        public OrderRequestBuilder limitPrice(BigDecimal limitPrice) { this.limitPrice = limitPrice; return this; }
        public OrderRequestBuilder stopPrice(BigDecimal stopPrice) { this.stopPrice = stopPrice; return this; }

        public OrderRequest build() {
            return new OrderRequest(this);
        }
    }
}