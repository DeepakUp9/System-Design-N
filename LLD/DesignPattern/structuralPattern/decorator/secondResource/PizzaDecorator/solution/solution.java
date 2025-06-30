package LLD.DesignPattern.structuralPattern.decorator.secondResource.PizzaDecorator.solution;

import LLD.DesignPattern.structuralPattern.decorator.secondResource.PizzaDecorator.solution.decorator.Cheese;
import LLD.DesignPattern.structuralPattern.decorator.secondResource.PizzaDecorator.solution.decorator.Olive;
import LLD.DesignPattern.structuralPattern.decorator.secondResource.PizzaDecorator.solution.plainPizza.ThickCrust;

public class solution {
    public static void main(String[] args) {
        ThickCrust base = new ThickCrust();
        System.out.println(base.getDescription()+" "+ base.getPrice());

        Cheese cheese = new Cheese(base);
        System.out.println(cheese.getDescription() + " " + cheese.getPrice());

        Olive olive = new Olive(cheese);
        System.out.println(olive.getDescription() + " " + olive.getPrice());
        
    }
}
