package LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem.Coffee.Americano;
import LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem.MilkDecorator.AlmontMilk;
import LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem.Coffee.Coffee;

public class Solution {
    public static void main(String[] args) {
       Americano amCoffee = new Americano();

       System.out.println(amCoffee.cost());
       System.out.println(amCoffee.description());
       System.out.println();

       Coffee amCoffeeWithAlmont = new AlmontMilk(amCoffee);
       System.out.println(amCoffeeWithAlmont.cost());
       System.out.println(amCoffeeWithAlmont.description());

    //    Coffee amCoffeeWithAlmontWithSugar = new SugarFree(amCoffeeWithAlmont);
    //    System.out.println(amCoffeeWithAlmontWithSugar.cost());
    //    System.out.println(amCoffeeWithAlmontWithSugar.description());



       


    }
}
