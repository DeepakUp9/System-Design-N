package com.library.observers;

import com.library.interfaces.NotificationObserver;
import com.library.models.Member;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Concrete Observer for email notifications.
 * 
 * DESIGN PATTERN: Observer Pattern - Concrete Observer
 * - Implements the NotificationObserver interface
 * - Handles email-specific notification logic
 * - In production, this would integrate with email service providers
 */
public class EmailNotificationObserver implements NotificationObserver {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public void update(Member member, String subject, String message) {
        sendEmail(member.getEmail(), subject, message);
    }
    
    @Override
    public String getChannelType() {
        return "EMAIL";
    }
    
    /**
     * Send email notification.
     * In production, this would integrate with SMTP server or email service API.
     * 
     * @param email Recipient email address
     * @param subject Email subject
     * @param message Email body
     */
    private void sendEmail(String email, String subject, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        
        System.out.println("=".repeat(80));
        System.out.println("📧 EMAIL NOTIFICATION SENT");
        System.out.println("=".repeat(80));
        System.out.println("Timestamp: " + timestamp);
        System.out.println("To: " + email);
        System.out.println("Subject: " + subject);
        System.out.println("-".repeat(80));
        System.out.println("Message:");
        System.out.println(message);
        System.out.println("=".repeat(80));
        System.out.println();
        
        // In production:
        // emailService.send(email, subject, message);
    }
}
