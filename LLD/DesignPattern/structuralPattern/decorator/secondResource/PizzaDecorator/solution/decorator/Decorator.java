package LLD.DesignPattern.structuralPattern.decorator.secondResource.PizzaDecorator.solution.decorator;

import LLD.DesignPattern.structuralPattern.decorator.secondResource.PizzaDecorator.solution.plainPizza.basePizza;

public class Decorator extends basePizza{
    basePizza pizza;

    @Override
    public String getDescription() {
        return this.pizza.getDescription() + ", " + description;
    }

    @Override
    public int getPrice() {
        return this.pizza.getPrice() + price;
    }
}
