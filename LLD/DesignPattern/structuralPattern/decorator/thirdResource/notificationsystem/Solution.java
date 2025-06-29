package testLLCode.Decoratorpattern.notificationsystem;

import testLLCode.Decoratorpattern.notificationsystem.FormateNotification.JsonNotification;
import testLLCode.Decoratorpattern.notificationsystem.LoggedNotification.CriticalNotification;
import testLLCode.Decoratorpattern.notificationsystem.Notification.Notification;
import testLLCode.Decoratorpattern.notificationsystem.Notification.Slack;

public class Solution {
    public static void main(String[] args) {
        Notification slackNotification = new Slack();
       
        System.out.println(slackNotification.sendSms(" testing@gmail.com ", " demo123@gmail.com "));

        Notification jsonNotification = new JsonNotification(slackNotification);
        System.out.println(jsonNotification.sendSms(" secondtesing@123 ", " random "));

        Notification crNotification = new CriticalNotification(jsonNotification);
        System.out.println(crNotification.sendSms("third", "nextrandomw"));
    }
}
