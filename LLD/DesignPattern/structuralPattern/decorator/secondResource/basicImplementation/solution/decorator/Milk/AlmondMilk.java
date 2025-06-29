package LLD.DesignPattern.structuralPattern.decorator.secondResource.basicImplementation.solution.decorator.Milk;

import LLD.DesignPattern.structuralPattern.decorator.secondResource.basicImplementation.solution.coffee.Coffee;

public class AlmondMilk extends Milk{
    
    public AlmondMilk(Coffee basecoffee) {
        coffee = basecoffee;
        cost = 30;
        description = "Almond Milk";
    }
}
