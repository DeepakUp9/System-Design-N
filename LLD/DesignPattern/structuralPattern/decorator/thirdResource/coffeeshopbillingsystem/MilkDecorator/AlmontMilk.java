package LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem.MilkDecorator;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem.Coffee.Coffee;

public class AlmontMilk extends MilkDecorator{

    public AlmontMilk(Coffee coffee) {
        super(coffee);
    }
    
    public double cost() {
        return coffee.cost() + 30;
    }

    @Override
    public String description() {
      return coffee.description() + " AlmontMilk";
    }

}
