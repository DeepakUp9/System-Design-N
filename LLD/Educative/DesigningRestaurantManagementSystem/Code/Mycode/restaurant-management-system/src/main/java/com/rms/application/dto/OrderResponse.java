package com.rms.application.dto;

import com.rms.core.model.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for returning order details to the client.
 */
@Data
@Builder
public class OrderResponse {
    private Long id;
    private Long branchId;
    private String channelType;
    private OrderStatus orderStatus;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}