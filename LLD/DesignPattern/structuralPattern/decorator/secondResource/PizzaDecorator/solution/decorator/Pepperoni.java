package LLD.DesignPattern.structuralPattern.decorator.secondResource.PizzaDecorator.solution.decorator;

import LLD.DesignPattern.structuralPattern.decorator.secondResource.PizzaDecorator.solution.plainPizza.basePizza;

public class Pepperoni extends Decorator{
    public Pepperoni(basePizza basePizza){
        pizza = basePizza;
        price = 29;
        description = "Pepperoni";
    }
}
