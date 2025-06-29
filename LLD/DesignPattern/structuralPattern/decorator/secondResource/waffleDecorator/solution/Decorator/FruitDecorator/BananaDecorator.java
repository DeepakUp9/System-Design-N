package LLD.DesignPattern.structuralPattern.decorator.secondResource.waffleDecorator.solution.Decorator.FruitDecorator;

import LLD.DesignPattern.structuralPattern.decorator.secondResource.waffleDecorator.solution.BaseWaffle.BaseWaffleClass;

public class BananaDecorator extends FruitDecorator{
    public BananaDecorator(BaseWaffleClass base){
        baseWaffle = base;
        description = "Banana";
        price = 38;
    }
}
