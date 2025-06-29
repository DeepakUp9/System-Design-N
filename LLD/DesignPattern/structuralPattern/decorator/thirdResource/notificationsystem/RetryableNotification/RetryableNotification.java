package testLLCode.Decoratorpattern.notificationsystem.RetryableNotification;

import testLLCode.Decoratorpattern.notificationsystem.NotificationDecorator;
import testLLCode.Decoratorpattern.notificationsystem.Notification.Notification;

public abstract class RetryableNotification extends NotificationDecorator{
    Notification notification;

    public RetryableNotification( Notification notification){
        this.notification = notification;
    }

    @Override
    public String sendSms(String to, String from) {
       return notification.sendSms(to, from) + "using RetryableNotification";
    }

}
