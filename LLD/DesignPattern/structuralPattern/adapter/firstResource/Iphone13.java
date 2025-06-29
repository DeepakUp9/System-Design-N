package LLD.DesignPattern.structuralPattern.adapter.firstResource;

public class Iphone13{

    private AppleCharger appleCharger;

    public Iphone13(AppleCharger appleCharger){
        this.appleCharger = appleCharger;
    }

    public void chargerIphone(){
         appleCharger.chargePhone();
    }
}