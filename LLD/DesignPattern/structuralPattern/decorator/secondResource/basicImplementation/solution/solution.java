package LLD.DesignPattern.structuralPattern.decorator.secondResource.basicImplementation.solution;

import LLD.DesignPattern.structuralPattern.decorator.secondResource.basicImplementation.solution.coffee.Nescafe;
import LLD.DesignPattern.structuralPattern.decorator.secondResource.basicImplementation.solution.decorator.Milk.AlmondMilk;
import LLD.DesignPattern.structuralPattern.decorator.secondResource.basicImplementation.solution.decorator.Milk.Milk;
import LLD.DesignPattern.structuralPattern.decorator.secondResource.basicImplementation.solution.decorator.Sugar.Sugar;

import LLD.DesignPattern.structuralPattern.decorator.secondResource.basicImplementation.solution.decorator.Sugar.BrownSugar;

public class solution {
    public static void main(String[] args) {
        
        Nescafe base = new Nescafe();
        System.out.println(base.getDescription() + " " + base.getCost());

        Milk extraMilk = new AlmondMilk(base);
        System.out.println(extraMilk.getDescription() + " " + extraMilk.getCost());

        Sugar extraSugar = new BrownSugar(extraMilk);
        System.out.println(extraSugar.getDescription() + " " + extraSugar.getCost());

        Milk extraExtraMilk = new AlmondMilk(extraSugar);
        System.out.println(extraExtraMilk.getDescription() + " " + extraExtraMilk.getCost());

    }
}
