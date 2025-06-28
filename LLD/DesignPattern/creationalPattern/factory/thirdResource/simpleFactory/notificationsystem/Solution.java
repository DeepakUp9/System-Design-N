package LLD.DesignPattern.creationalPattern.factory.thirdResource.simpleFactory.notificationsystem;

class Solution {
    
    public static void main(String []args){
        Notification email = NotificationFactory.getInstanceOfNotification("email");
        email.sendSMS("secure message....");

        Notification push = NotificationFactory.getInstanceOfNotification("push");
        push.sendSMS("time pass....");

    }
}