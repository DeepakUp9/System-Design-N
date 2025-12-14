package com.stockbrokerage.notification;

import com.stockbrokerage.order.event.OrderProcessedEvent;
import com.stockbrokerage.order.enums.OrderStatus;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Concrete Observer 2: Handles customer-facing notifications.
 */
@Service
public class NotificationServiceListener {

    // Dependency Injection (e.g., EmailService, SmsGatewayService, Decorator/Strategy for channel)

    @EventListener
    @Async
    public void handleOrderEvent(OrderProcessedEvent event) {

        // We only notify the user on key, terminal, or actionable states.
        if (event.getCurrentStatus() == OrderStatus.EXECUTED ||
                event.getCurrentStatus() == OrderStatus.REJECTED ||
                event.getCurrentStatus() == OrderStatus.CANCELLED) {

            String message = String.format("Notification Sent: Your order %s for %s has been %s.",
                    event.getOrder().getOrderReferenceId(),
                    event.getOrder().getSymbol(),
                    event.getCurrentStatus());

            System.out.printf("[NOTIFICATION - ASYNC] To User %d: %s%n", event.getOrder().getAccountId(), message);

            // Production Logic:
            // 1. Check user preferences (Email, SMS, Push).
            // 2. Use a Decorator Pattern (future step) to wrap notification channels (Emailer, SMSer) with logging/retry features.
            // 3. Send the notification via the appropriate channel.

        } else {
            // For PENDING_VALIDATION, PENDING_EXECUTION, etc., we generally don't notify the client immediately.
            System.out.printf("[NOTIFICATION - INFO] No user notification required for status: %s%n", event.getCurrentStatus());
        }
    }
}