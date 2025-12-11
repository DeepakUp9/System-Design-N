package com.rms.application.service;

import com.rms.application.event.OrderPlacedEvent;
import com.rms.application.exception.OrderStateException;
import com.rms.application.exception.ResourceNotFoundException;
import com.rms.application.factory.FulfillmentStrategyFactory;
import com.rms.application.model.Order;
import com.rms.application.repository.OrderRepository;
import com.rms.core.context.OrderContext;
import com.rms.core.model.OrderStatus;
import com.rms.core.state.OrderState;
import com.rms.core.strategy.PricingStrategy;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * The transactional service layer that manages the Order lifecycle.
 * It is responsible for instantiating the LLD pattern components and ensuring persistence.
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final FulfillmentStrategyFactory strategyFactory;
    private final ApplicationEventPublisher eventPublisher;
    private final Map<String, PricingStrategy> pricingStrategyMap;

    // Spring injects ALL concrete OrderState beans into this Map for runtime lookup
    private final Map<String, OrderState> orderStateMap;

    // Constructor Injection (Wiring all dependencies)
    public OrderService(
            OrderRepository orderRepository,
            FulfillmentStrategyFactory strategyFactory,
            Map<String, OrderState> orderStateMap, // Key: bean name (e.g., "newOrderState"), Value: State Object
            ApplicationEventPublisher eventPublisher,
            Map<String, PricingStrategy> pricingStrategyMap
    ) {
        this.orderRepository = orderRepository;
        this.strategyFactory = strategyFactory;
        this.orderStateMap = orderStateMap;
        this.eventPublisher = eventPublisher;
        this.pricingStrategyMap = pricingStrategyMap;
    }

    // --- Helper for State Management ---

    /**
     * Finds the correct OrderState bean from the map based on the status name.
     * @param status The current OrderStatus enum.
     * @return The corresponding concrete OrderState bean.
     */
    private OrderState getStateBean(OrderStatus status) {
        String beanName = status.name().toLowerCase() + "state";
        OrderState state = orderStateMap.get(beanName);
        if (state == null) {
            throw new IllegalStateException("Missing bean for state: " + status.name());
        }
        return state;
    }

    // --- Core Business Logic Methods ---

    /**
     * Initializes the OrderContext and processes the order (State Pattern's process()).
     * This method is transactional to ensure state changes and fulfillment execution are atomc.
     * @param orderId The ID of the order to process.
     * @param channelType The strategy to use, e.g., "ONLINE_DELIVERY".
     * @return The updated Order entity.
     */
    @Transactional
    public Order processNewOrder(Long orderId, String channelType) {
        Order orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId)); // Use custom exception

        // Edge Case: Prevent reprocessing an order that is already processing/complete
        if (orderEntity.getOrderStatus() != OrderStatus.NEW) {
            throw new OrderStateException("Order " + orderId + " is already in state: " + orderEntity.getOrderStatus());
        }

        // 1. Initialize Context with current state
        OrderContext context = new OrderContext(orderId);
        context.setStatus(orderEntity.getOrderStatus());
        context.changeState(getStateBean(orderEntity.getOrderStatus()));

        // 2. Execute State Transition Logic (Process)
        context.process(); // Logic executed in NewOrderState

        // 3. Update Order Entity with the new status from the Context
        orderEntity.setOrderStatus(OrderStatus.PROCESSING); // New state after processing

        // 4. Execute Fulfillment Strategy (Strategy Pattern)
        // Note: Strategy is executed after initial state transition for a NEW order.
        strategyFactory.getStrategy(channelType).executeFulfillment(context);

        // Save the updated entity and commit the transaction
        return orderRepository.save(orderEntity);
    }

    /**
     * Executes the fulfill transition (State Pattern's fulfill()).
     * @param orderId The ID of the order to fulfill.
     * @return The updated Order entity.
     */
    @Transactional
    public Order fulfillOrder(Long orderId) {
        Order orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));

        // 1. Initialize Context with current state
        OrderContext context = new OrderContext(orderId);
        context.setStatus(orderEntity.getOrderStatus());
        context.changeState(getStateBean(orderEntity.getOrderStatus()));

        // 2. Execute State Transition Logic (Fulfill)
        context.fulfill(); // Logic executed in ProcessingState -> ReadyForServiceState

        // 3. Update Order Entity with the new status
        // We simulate the next state for now, but in a real-world scenario,
        // the state transition logic in the state class would set the next state (e.g. READY_FOR_SERVICE)
        orderEntity.setOrderStatus(OrderStatus.READY_FOR_SERVICE);

        // Save the updated entity
        return orderRepository.save(orderEntity);
    }

    /**
     * Calculates the final order total using the Strategy Pattern.
     * @param baseTotal The sum of all item prices.
     * @param strategyType e.g., "DEFAULT", "PROMO_10".
     * @return The final total price including tax and surcharges/discounts.
     */
    public BigDecimal calculateOrderTotal(BigDecimal baseTotal, String strategyType) {

        // Find the strategy bean based on the type (e.g., "defaultPricingStrategy")
        String beanName = strategyType.toLowerCase() + "strategy";
        PricingStrategy strategy = pricingStrategyMap.get(beanName);

        if (strategy == null) {
            // Edge Case: Fallback to the default strategy if the requested one is invalid
            System.err.println("Pricing Strategy not found: " + strategyType + ". Falling back to DEFAULT.");
            strategy = pricingStrategyMap.get("defaultPricingStrategy");
        }

        System.out.println("Strategy Context: Calculating total using " + strategy.getType() + " strategy.");
        return strategy.calculateFinalPrice(baseTotal);
    }

    // Placeholder for a real order creation method
    public Order createOrder(Order order) {
        // Assume this sets up the initial NEW order status and persists it
// Calculate the FINAL price before persisting
        BigDecimal finalTotal = calculateOrderTotal(order.getTotalAmount(), "dummyStartegy");
        order.setTotalAmount(finalTotal);

        // 1. Set initial status and persist
        order.setOrderStatus(OrderStatus.NEW);
        Order savedOrder = orderRepository.save(order);

        // 2. Publish the event to notify observers
        // The transaction listener ensures this only fires after a successful commit.
        eventPublisher.publishEvent(new OrderPlacedEvent(this, savedOrder));

        return savedOrder;
    }

}