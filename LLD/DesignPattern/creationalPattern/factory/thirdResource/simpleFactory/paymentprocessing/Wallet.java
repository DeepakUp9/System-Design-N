package LLD.DesignPattern.creationalPattern.factory.thirdResource.simpleFactory.paymentprocessing;


class Wallet implements PaymentProcessing {
    public void dotxn(){
        System.out.println("making payment using Wallet");
    }
}