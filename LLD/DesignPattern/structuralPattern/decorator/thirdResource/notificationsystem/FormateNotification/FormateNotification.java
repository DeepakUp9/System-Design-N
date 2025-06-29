package testLLCode.Decoratorpattern.notificationsystem.FormateNotification;

import testLLCode.Decoratorpattern.notificationsystem.NotificationDecorator;
import testLLCode.Decoratorpattern.notificationsystem.Notification.Notification;

public class FormateNotification extends NotificationDecorator{
    Notification notification;

    public FormateNotification( Notification notification){
        this.notification = notification;
    }

    @Override
    public String sendSms(String to, String from) {
       return notification.sendSms(to, from) + "using FormateNotification";
    }

}