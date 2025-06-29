package LLD.DesignPattern.structuralPattern.decorator.secondResource.basicImplementation.solution.decorator.Milk;

import LLD.DesignPattern.structuralPattern.decorator.secondResource.basicImplementation.solution.coffee.Coffee;

public class CowMilk extends Milk{

    public CowMilk(Coffee basecoffee) {
        coffee = basecoffee;
        cost = 26;
        description = "Cow Milk";
    }

}