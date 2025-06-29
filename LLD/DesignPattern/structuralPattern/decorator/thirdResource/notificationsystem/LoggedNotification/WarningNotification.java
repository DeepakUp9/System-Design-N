package testLLCode.Decoratorpattern.notificationsystem.LoggedNotification;

import testLLCode.Decoratorpattern.notificationsystem.Notification.Notification;

public class WarningNotification extends LoggedNotification{

    public WarningNotification(Notification notification) {
        super(notification);
    }
    @Override
    public String sendSms(String to, String from) {
       return super.sendSms(to, from) + "using WarningNotification";
    }
}
