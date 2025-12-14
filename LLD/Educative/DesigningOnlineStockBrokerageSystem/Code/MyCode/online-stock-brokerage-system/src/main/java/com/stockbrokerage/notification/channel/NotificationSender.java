package com.stockbrokerage.notification.channel;

import com.stockbrokerage.order.model.Order;

/**
 * Component Interface: Defines the core contract for sending a notification.
 */
public interface NotificationSender {
    void send(Order order, String message);
}