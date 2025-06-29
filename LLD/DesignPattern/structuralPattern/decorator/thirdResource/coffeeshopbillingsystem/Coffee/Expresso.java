package LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem.Coffee;

public class Expresso extends Coffee {

    @Override
   public double cost() {
        return 100;
    }

    @Override
    public String description() {
      return this.desription + "Expresso";
    }
    
}
