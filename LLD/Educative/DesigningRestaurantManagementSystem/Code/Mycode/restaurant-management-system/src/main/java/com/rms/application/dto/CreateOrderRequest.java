package com.rms.application.dto;

import com.rms.core.model.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO for creating a new order. Includes validation annotations.
 */
@Data
public class CreateOrderRequest {

    @NotNull(message = "Branch ID is mandatory for multi-branch operation")
    @Positive
    private Long branchId;

    @NotBlank(message = "Channel type (DINE_IN/ONLINE_DELIVERY) is mandatory")
    private String channelType; // Used by FulfillmentStrategyFactory

    @NotNull
    @Positive(message = "Total amount must be positive")
    private BigDecimal totalAmount;

    // In a real system, line items (menu items, quantity) would be here.
}