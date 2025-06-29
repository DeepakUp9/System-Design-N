package LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem.Coffee.Coffee;

public abstract class CoffeeDecorator extends Coffee {
     
    public CoffeeDecorator(){
        this.desription += "CoffeeDecorator";
    }
   
}
