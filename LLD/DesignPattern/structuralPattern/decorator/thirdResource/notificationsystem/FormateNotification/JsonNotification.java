package testLLCode.Decoratorpattern.notificationsystem.FormateNotification;

import testLLCode.Decoratorpattern.notificationsystem.Notification.Notification;

public class JsonNotification extends FormateNotification{

    public JsonNotification(Notification notification) {
        super(notification);
    }

    @Override
    public String sendSms(String to, String from) {
       return super.sendSms(to, from) + "using JsonNotification";
    }
    
}
