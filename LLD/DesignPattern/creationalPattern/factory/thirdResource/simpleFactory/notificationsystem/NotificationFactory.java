package LLD.DesignPattern.creationalPattern.factory.thirdResource.simpleFactory.notificationsystem;

class NotificationFactory {
    
    public static Notification getInstanceOfNotification(String type){
        if(type.trim().equals("email")){
          return new Email();
        }else if (type.trim().equals("sms")){
            return new SMS();
        }else if (type.trim().equals("push")){
            return new Push();
        }else {
            return null;
        }
    }
}