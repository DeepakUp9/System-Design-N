package LLD.DesignPattern.structuralPattern.decorator.secondResource.waffleDecorator.solution.Decorator.ChocolateDecorator;

import LLD.DesignPattern.structuralPattern.decorator.secondResource.waffleDecorator.solution.BaseWaffle.BaseWaffleClass;

public class CaramelChocolate extends ChocolateDecorator {

    public CaramelChocolate(BaseWaffleClass base) {
        baseWaffle = base;
        description = "Caramel";
        price = 40;
    }
}
