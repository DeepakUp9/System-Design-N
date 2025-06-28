package LLD.DesignPattern.creationalPattern.factory.thirdResource.simpleFactory.paymentprocessing;


class Solution {
    
    public static void main(String [] args){
         PaymentProcessing creditCard  = PaymentProcessingFactory.paymentProcesserInstace("CreditedCard");
         creditCard.dotxn();

         PaymentProcessing wallet  = PaymentProcessingFactory.paymentProcesserInstace("Wallet");
         wallet.dotxn();

    }
}