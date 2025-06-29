package LLD.DesignPattern.structuralPattern.adapter.firstResource;

public class ChargerXYZ implements AppleCharger{

    @Override
    public void chargePhone(){
       System.out.println("Your iphone is charging");
    }
}