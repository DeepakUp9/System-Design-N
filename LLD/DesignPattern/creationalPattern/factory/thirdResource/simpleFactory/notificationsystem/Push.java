package LLD.DesignPattern.creationalPattern.factory.thirdResource.simpleFactory.notificationsystem;
class Push implements Notification{
    public void sendSMS(String message){
        System.out.println("Sending message using push and the message is:" + message);
    }
}