package com.stockbrokerage.notification.channel;

import com.stockbrokerage.order.model.Order;
import org.springframework.stereotype.Service;

/**
 * Concrete Component: The basic email sending functionality.
 */
@Service("emailSender")
public class EmailSender implements NotificationSender {

    @Override
    public void send(Order order, String message) {
        System.out.printf("[CORE EMAIL] Sending email to Account %d: %s%n", order.getAccountId(), message);
        // Production: Integrate with SendGrid, AWS SES, etc.
    }
}