package LLD.DesignPattern.creationalPattern.factory.thirdResource.simpleFactory.paymentprocessing;

class DebitCard implements PaymentProcessing {
    public void dotxn(){
        System.out.println("making payment using DebitCard");
    }
}