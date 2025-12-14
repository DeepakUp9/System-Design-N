package com.stockbrokerage.finance.service;

import com.stockbrokerage.order.model.Order;
import com.stockbrokerage.order.enums.OrderStatus;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Service Layer: Handles the T+2 settlement cycle for US equities.
 * This service would track trades and finalize funds/securities transfer after the settlement date.
 */
@Service
public class SettlementService {

    /**
     * Calculates the T+2 (Trade date + 2 business days) settlement date.
     * @param tradeDate The date the trade was executed.
     * @return The calculated settlement date.
     */
    public LocalDate calculateSettlementDate(LocalDateTime tradeDate) {
        LocalDate settlementDate = tradeDate.toLocalDate();
        int days = 0;
        while (days < 2) {
            settlementDate = settlementDate.plusDays(1);
            // Skip weekends; in production, we would check for holidays too.
            if (settlementDate.getDayOfWeek() != DayOfWeek.SATURDAY &&
                    settlementDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                days++;
            }
        }
        return settlementDate;
    }

    /**
     * Marks an order as pending settlement.
     * @param order The executed order.
     */
    public void markTradeForSettlement(Order order) {
        LocalDate settlementDate = calculateSettlementDate(order.getExecutedAt());

        // CRITICAL NOTE: In a real system, the status would become PENDING_SETTLEMENT.
        // For simplicity, we assume immediate execution and set the actual cash/share movement to the settlement service.

        System.out.printf("[SETTLEMENT] Trade %s marked for settlement on %s.%n",
                order.getOrderReferenceId(), settlementDate);

        // Production: Persist a SettlementRecord entity.
    }
}