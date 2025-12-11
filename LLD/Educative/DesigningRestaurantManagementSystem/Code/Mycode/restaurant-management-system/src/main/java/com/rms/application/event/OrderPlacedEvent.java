package com.rms.application.event;

import com.rms.application.model.Order;
import org.springframework.context.ApplicationEvent;

/**
 * The Event (Subject Notification): Carries data about the new order.
 */
public class OrderPlacedEvent extends ApplicationEvent {

    private final Order order;

    // source is the object on which the Event initially occurred
    public OrderPlacedEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}