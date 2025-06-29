package LLD.DesignPattern.structuralPattern.decorator.secondResource.waffleDecorator.solution.Decorator.FruitDecorator;

import LLD.DesignPattern.structuralPattern.decorator.secondResource.waffleDecorator.solution.BaseWaffle.BaseWaffleClass;

public class AppleDecorator extends FruitDecorator{

    public AppleDecorator(BaseWaffleClass base){
        baseWaffle = base;
        description = "Apple";
        price = 37;
    }
    
}
