package LLD.DesignPattern.structuralPattern.decorator.secondResource.waffleDecorator.solution.Decorator.FruitDecorator;

import LLD.DesignPattern.structuralPattern.decorator.secondResource.waffleDecorator.solution.BaseWaffle.BaseWaffleClass;

public class FruitDecorator extends BaseWaffleClass{
    BaseWaffleClass baseWaffle;

    @Override
    public String getDescription() {
        return this.baseWaffle.getDescription() + ", " + description;
    }

    @Override
    public int getPrice() {
        return this.baseWaffle.getPrice() + price;
    }
}
