package LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem.SugarDecorator;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem.Coffee.Coffee;

public class SugarNormal extends SugarDecorator{

    SugarNormal(Coffee coffee) {
        super(coffee);
    }

     public double cost() {
        return coffee.cost() + 8;
    }

    public String description() {
      return coffee.description() + "SugarNormal";
    }
    
    
}
