package testLLCode.Decoratorpattern.notificationsystem.LoggedNotification;

import testLLCode.Decoratorpattern.notificationsystem.NotificationDecorator;
import testLLCode.Decoratorpattern.notificationsystem.Notification.Notification;

public class LoggedNotification extends NotificationDecorator{
    Notification notification;

    public LoggedNotification( Notification notification){
        this.notification = notification;
    }

    @Override
    public String sendSms(String to, String from) {
       return notification.sendSms(to, from) + "using LoggedNotification";
    }
}
