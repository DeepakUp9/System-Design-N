package LLD.DesignPattern.structuralPattern.decorator.secondResource.basicImplementation.solution.decorator.Sugar;

import LLD.DesignPattern.structuralPattern.decorator.secondResource.basicImplementation.solution.coffee.Coffee;

public class BrownSugar extends Sugar{
    
    public BrownSugar(Coffee basCoffee){
        coffee = basCoffee;
        cost = 17;
        description = "Brown Sugar";
    }

}
