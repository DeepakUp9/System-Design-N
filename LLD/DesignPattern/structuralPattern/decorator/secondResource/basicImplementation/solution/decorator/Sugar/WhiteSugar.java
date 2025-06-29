package LLD.DesignPattern.structuralPattern.decorator.secondResource.basicImplementation.solution.decorator.Sugar;

import LLD.DesignPattern.structuralPattern.decorator.secondResource.basicImplementation.solution.coffee.Coffee;

public class WhiteSugar extends Sugar{
    
    public WhiteSugar(Coffee basCoffee){
        coffee = basCoffee;
        cost = 23;
        description = "White Sugar";
    }

}
