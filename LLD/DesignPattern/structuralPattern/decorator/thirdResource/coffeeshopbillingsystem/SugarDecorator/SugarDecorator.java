package LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem.SugarDecorator;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem.CoffeeDecorator;
import LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem.Coffee.Coffee;

abstract class SugarDecorator extends CoffeeDecorator{
     Coffee coffee;

    public SugarDecorator(Coffee coffee){
        this.coffee = coffee;
        this.desription += "SugarDecorator";
    }
    
   
}
