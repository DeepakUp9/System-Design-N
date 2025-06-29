package LLD.DesignPattern.structuralPattern.decorator.secondResource.basicImplementation.solution.decorator;

import LLD.DesignPattern.structuralPattern.decorator.secondResource.basicImplementation.solution.coffee.Coffee;

public abstract class BaseDecorator extends Coffee{
    protected Coffee coffee;

    @Override
    public int getCost() {
        return this.coffee.getCost() + cost;
    }

    @Override
    public String getDescription() {
        return this.coffee.getDescription() + ", " + description;
    }
}
