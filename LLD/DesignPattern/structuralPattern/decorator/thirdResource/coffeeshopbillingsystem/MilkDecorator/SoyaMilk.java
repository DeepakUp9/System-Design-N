package LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem.MilkDecorator;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem.Coffee.Coffee;

public class SoyaMilk extends MilkDecorator {
     SoyaMilk(Coffee coffee) {
        super(coffee);
    }
    
    public double cost() {
        return coffee.cost() + 12;
    }

    public String description() {
      return coffee.description() + "SoyaMilk";
    }
    
}
