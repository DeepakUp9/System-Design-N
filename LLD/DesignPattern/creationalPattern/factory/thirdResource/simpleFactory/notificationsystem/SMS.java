package LLD.DesignPattern.creationalPattern.factory.thirdResource.simpleFactory.notificationsystem;


class SMS implements Notification{
    public void sendSMS(String message){
        System.out.println("Sending message using SMS and the message is:" + message);
    }
}