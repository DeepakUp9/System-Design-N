package testLLCode.Decoratorpattern.notificationsystem.LoggedNotification;

import testLLCode.Decoratorpattern.notificationsystem.Notification.Notification;

public class CriticalNotification extends LoggedNotification{

    public CriticalNotification(Notification notification) {
        super(notification);
    }
    @Override
    public String sendSms(String to, String from) {
       return super.sendSms(to, from) + "using CriticalNotification";
    }
}
