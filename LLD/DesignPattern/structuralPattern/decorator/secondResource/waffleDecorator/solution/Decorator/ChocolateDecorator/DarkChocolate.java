package LLD.DesignPattern.structuralPattern.decorator.secondResource.waffleDecorator.solution.Decorator.ChocolateDecorator;

import LLD.DesignPattern.structuralPattern.decorator.secondResource.waffleDecorator.solution.BaseWaffle.BaseWaffleClass;

public class DarkChocolate extends ChocolateDecorator{

    public DarkChocolate(BaseWaffleClass base){
        baseWaffle = base;
        description = "DarkChocolate";
        price = 55;
    }
}
