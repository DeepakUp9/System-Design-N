package com.stockbrokerage.order.service;

import com.stockbrokerage.OnlineStockBrokerageSystemApplication;
import com.stockbrokerage.order.enums.OrderStatus;
import com.stockbrokerage.order.enums.OrderType;
import com.stockbrokerage.order.model.Order;
import com.stockbrokerage.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration Test: Verifies the correct Strategy is selected and executed
 * through the OrderExecutionService (the Strategy Context).
 */
@SpringBootTest(classes = OnlineStockBrokerageSystemApplication.class)
public class OrderExecutionServiceIntegrationTest {

    @Autowired
    private OrderExecutionService orderExecutionService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @Transactional
    void testMarketOrderExecution_ShouldExecuteImmediately() {
        // Arrange: Create a Market Order
        Order marketOrder = new Order(
                null, null, 1001L, "AAPL", OrderType.MARKET,
                new BigDecimal("10.0"), new BigDecimal("150.00"),
                null, null, OrderStatus.NEW, null, null
        );

        // Act: Process the order via the Context
        Order executedOrder = orderExecutionService.processOrder(marketOrder);

        // Assert: Verify the MarketOrderStrategy logic was applied
        assertThat(executedOrder.getStatus()).isEqualTo(OrderStatus.EXECUTED);
        assertThat(executedOrder.getExecutedAt()).isNotNull();

        // Assert: Verify persistence
        assertThat(orderRepository.findById(executedOrder.getId())).isPresent();
    }

    @Test
    @Transactional
    void testLimitOrderExecution_ShouldBePending() {
        // Arrange: Create a Limit Order
        Order limitOrder = new Order(
                null, null, 1002L, "GOOGL", OrderType.LIMIT,
                new BigDecimal("5.0"), new BigDecimal("2500.00"),
                new BigDecimal("2450.00"), null, OrderStatus.NEW, null, null
        );

        // Act: Process the order via the Context
        Order pendingOrder = orderExecutionService.processOrder(limitOrder);

        // Assert: Verify the LimitOrderStrategy logic was applied
        // Limit orders are expected to be set to PENDING_EXECUTION by their strategy
        assertThat(pendingOrder.getStatus()).isEqualTo(OrderStatus.PENDING_EXECUTION);
        assertThat(pendingOrder.getExecutedAt()).isNull();
    }
}