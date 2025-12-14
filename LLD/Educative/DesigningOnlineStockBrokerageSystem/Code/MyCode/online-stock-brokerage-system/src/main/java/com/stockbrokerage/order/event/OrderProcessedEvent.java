package com.stockbrokerage.order.event;

import com.stockbrokerage.order.model.Order;
import com.stockbrokerage.order.enums.OrderStatus;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * The Event Object: Carries the data about the order status change.
 */
@Getter
public class OrderProcessedEvent extends ApplicationEvent {

    private final Order order;
    private final OrderStatus previousStatus;
    private final OrderStatus currentStatus;
    private final LocalDateTime timestamp;

    /**
     * @param source The object that published the event (usually 'this').
     * @param order The full Order entity.
     * @param previousStatus The status before the transition.
     */
    public OrderProcessedEvent(Object source, Order order, OrderStatus previousStatus) {
        super(source);
        this.order = order;
        this.previousStatus = previousStatus;
        this.currentStatus = order.getStatus();
        this.timestamp = LocalDateTime.now();
    }
}