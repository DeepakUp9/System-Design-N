package LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem.SugarDecorator;
import LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem.Coffee.Coffee;


public class SugarFree extends SugarDecorator{

    public SugarFree(Coffee coffee) {
        super(coffee);
    }

     public double cost() {
        return coffee.cost() + 5;
    }

    public String description() {
      return coffee.description() + "SugarFree";
    }
    
    
}
