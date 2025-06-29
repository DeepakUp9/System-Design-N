package testLLCode.Decoratorpattern.notificationsystem.RetryableNotification;

import testLLCode.Decoratorpattern.notificationsystem.Notification.Notification;

public class MinRetry extends RetryableNotification {

    public MinRetry(Notification notification) {
        super(notification);
    }

    @Override
    public String sendSms(String to, String from) {
       return super.sendSms(to, from) + "using MinRetry";
    }
}
