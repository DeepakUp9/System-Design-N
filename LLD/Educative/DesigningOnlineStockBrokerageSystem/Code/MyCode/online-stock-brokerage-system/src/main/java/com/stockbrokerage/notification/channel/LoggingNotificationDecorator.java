package com.stockbrokerage.notification.channel;

import com.stockbrokerage.order.model.Order;

/**
 * Concrete Decorator: Adds mandatory logging/auditing before sending the notification.
 */
public class LoggingNotificationDecorator extends NotificationDecorator {

    public LoggingNotificationDecorator(NotificationSender wrappedSender) {
        super(wrappedSender);
    }

    @Override
    public void send(Order order, String message) {
        // NEW FEATURE: Pre-processing (logging)
        logMessage(order, message);

        // DELEGATION: Call the original sender (or the next decorator)
        super.send(order, message);
    }

    private void logMessage(Order order, String message) {
        System.out.printf("[DECORATOR LOG] Logged notification attempt for Ref %s. Message: %s%n",
                order.getOrderReferenceId(), message.substring(0, Math.min(message.length(), 60)) + "...");
        // Production: Persist log entry to a log database.
    }
}