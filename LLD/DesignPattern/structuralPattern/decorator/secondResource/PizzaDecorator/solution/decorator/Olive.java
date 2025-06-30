package LLD.DesignPattern.structuralPattern.decorator.secondResource.PizzaDecorator.solution.decorator;

import LLD.DesignPattern.structuralPattern.decorator.secondResource.PizzaDecorator.solution.plainPizza.basePizza;

public class Olive extends Decorator {

    public Olive(basePizza basePizza) {
        pizza = basePizza;
        price = 41; // decorator
        description = "Olive"; // decorator
    }
}