package com.library.observers;

import com.library.interfaces.NotificationObserver;
import com.library.models.Member;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Concrete Observer for SMS notifications.
 * 
 * DESIGN PATTERN: Observer Pattern - Concrete Observer
 * - Implements the NotificationObserver interface
 * - Handles SMS-specific notification logic
 * - In production, this would integrate with SMS service providers (Twilio, etc.)
 */
public class SMSNotificationObserver implements NotificationObserver {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int MAX_SMS_LENGTH = 160;
    
    @Override
    public void update(Member member, String subject, String message) {
        sendSMS(member.getPhone(), subject, message);
    }
    
    @Override
    public String getChannelType() {
        return "SMS";
    }
    
    /**
     * Send SMS notification.
     * In production, this would integrate with SMS gateway API.
     * 
     * @param phone Recipient phone number
     * @param subject SMS subject (prefix)
     * @param message SMS message body
     */
    private void sendSMS(String phone, String subject, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String smsContent = subject + ": " + message;
        
        // Truncate if necessary
        if (smsContent.length() > MAX_SMS_LENGTH) {
            smsContent = smsContent.substring(0, MAX_SMS_LENGTH - 3) + "...";
        }
        
        System.out.println("=".repeat(80));
        System.out.println("📱 SMS NOTIFICATION SENT");
        System.out.println("=".repeat(80));
        System.out.println("Timestamp: " + timestamp);
        System.out.println("To: " + phone);
        System.out.println("-".repeat(80));
        System.out.println("Message:");
        System.out.println(smsContent);
        System.out.println("=".repeat(80));
        System.out.println();
        
        // In production:
        // smsService.send(phone, smsContent);
    }
}
