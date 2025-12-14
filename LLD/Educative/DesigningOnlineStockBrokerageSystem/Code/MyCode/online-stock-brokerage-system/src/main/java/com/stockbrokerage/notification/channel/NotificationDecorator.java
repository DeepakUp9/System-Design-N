package com.stockbrokerage.notification.channel;

import com.stockbrokerage.order.model.Order;
import lombok.RequiredArgsConstructor;

/**
 * Abstract Decorator: Holds a reference to the wrapped Component/Decorator.
 */
@RequiredArgsConstructor
public abstract class NotificationDecorator implements NotificationSender {

    // Reference to the object being decorated (Component or another Decorator)
    protected final NotificationSender wrappedSender;

    @Override
    public void send(Order order, String message) {
        // Delegates the call to the wrapped object
        wrappedSender.send(order, message);
    }
}