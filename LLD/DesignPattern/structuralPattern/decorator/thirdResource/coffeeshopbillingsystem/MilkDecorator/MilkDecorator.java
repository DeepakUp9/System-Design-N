package LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem.MilkDecorator;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem.CoffeeDecorator;
import LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem.Coffee.Coffee;

abstract class MilkDecorator extends CoffeeDecorator{
    Coffee coffee;

    public MilkDecorator(Coffee coffee){
        this.coffee = coffee;
        this.desription += "MilkDecorator";
    }
    
}
