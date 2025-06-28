package LLD.DesignPattern.creationalPattern.factory.thirdResource.simpleFactory.paymentprocessing;

class CreditCard implements PaymentProcessing {
    public void dotxn(){
        System.out.println("making payment using CreditCard");
    }
}