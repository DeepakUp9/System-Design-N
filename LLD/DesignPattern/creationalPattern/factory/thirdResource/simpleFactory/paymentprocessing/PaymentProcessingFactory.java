package LLD.DesignPattern.creationalPattern.factory.thirdResource.simpleFactory.paymentprocessing;

import java.util.HashMap;
class PaymentProcessingFactory {
    
    private static HashMap<String, PaymentProcessing> map = new HashMap<>();

    public static PaymentProcessing paymentProcesserInstace(String type){
        if(map.containsKey(type)) return map.get(type);
        else {
            PaymentProcessing obj = addInstanceInMap(type);
            map.put(type, obj);
        }
        return map.get(type);
    }
    private static PaymentProcessing addInstanceInMap(String type){
        if(type.trim().equals("CreditedCard")){
            return new CreditCard();
        }else if (type.trim().equals("DebitCard")){
            return new DebitCard();
        }else if (type.trim().equals("Wallet")){
            return new Wallet();
        }else {
            return null;
        }
    }
}