package testLLCode.Decoratorpattern.notificationsystem.RetryableNotification;

import testLLCode.Decoratorpattern.notificationsystem.Notification.Notification;

public class MaxRetry extends RetryableNotification {

    public MaxRetry(Notification notification) {
        super(notification);
    }

    @Override
    public String sendSms(String to, String from) {
       return super.sendSms(to, from) + "using MaxRetry";
    }
}
