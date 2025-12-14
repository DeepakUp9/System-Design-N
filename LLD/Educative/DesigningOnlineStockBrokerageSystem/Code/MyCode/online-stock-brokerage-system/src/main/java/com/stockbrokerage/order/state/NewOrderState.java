package com.stockbrokerage.order.state;

import com.stockbrokerage.order.exceptions.OrderProcessingException;
import com.stockbrokerage.order.model.Order;
import com.stockbrokerage.order.enums.OrderStatus;
import com.stockbrokerage.risk.service.RiskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Concrete State: Handles the logic and allowed transitions from the NEW status.
 */
@Service
@RequiredArgsConstructor
public class NewOrderState extends AbstractOrderState {

    private final RiskService riskService; // NEW: Injected RiskService

    @Override
    protected OrderStatus getTargetStatus() {
        return OrderStatus.NEW;
    }

    @Override
    public OrderState validate(Order order) {
        try {
            // **CORE INTEGRATION:** Run all pre-trade checks
            riskService.runPreTradeChecks(order);

            // If checks pass, transition to the next state
            transition(order, OrderStatus.PENDING_VALIDATION, "Risk and compliance checks passed.");

            // In a full implementation, this should return the PENDING_VALIDATION state object from the manager.
            return null;

        } catch (OrderProcessingException e) {
            // If checks fail, transition to REJECTED
            transition(order, OrderStatus.REJECTED, "Order rejected by risk engine: " + e.getMessage());
            // This should return the REJECTED state object from the manager.
            return null;
        }
    }

    @Override
    public OrderState process(Order order) {
        // Must validate first. Order cannot be processed from NEW state.
        System.err.println("CRITICAL: Attempted to process order from NEW state. Forcing validation first.");
        return validate(order);
    }

    // Cancel override is inherited from AbstractOrderState for simplicity
}