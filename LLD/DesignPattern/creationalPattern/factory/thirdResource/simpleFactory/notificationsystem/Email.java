package LLD.DesignPattern.creationalPattern.factory.thirdResource.simpleFactory.notificationsystem;


class Email implements Notification{
    public void sendSMS(String message){
        System.out.println("Sending message using email and the message is:" + message);
    }
}