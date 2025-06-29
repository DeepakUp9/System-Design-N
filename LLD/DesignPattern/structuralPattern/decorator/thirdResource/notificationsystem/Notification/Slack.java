package testLLCode.Decoratorpattern.notificationsystem.Notification;

public class Slack extends Notification{

    @Override
    public String sendSms(String to, String from) {
       return "Sending" + from + "to the" + to  + "using Slack";
    }
    
}
