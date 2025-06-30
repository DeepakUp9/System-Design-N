package LLD.DesignPattern.structuralPattern.decorator.secondResource.PizzaDecorator.solution.decorator;

import LLD.DesignPattern.structuralPattern.decorator.secondResource.PizzaDecorator.solution.plainPizza.basePizza;

public class Cheese extends Decorator{
    
    public Cheese(basePizza basePizza){
        pizza = basePizza;
        price = 35; //decorator
        description = "Cheese"; // decorator
    }
}
