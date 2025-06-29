package LLD.DesignPattern.structuralPattern.decorator.thirdResource.PIZZA;

public class Solution {
    public static void main(String[] args) {
        Pizza pizza = new NormalBase();
       
        System.out.println(pizza.cost());
        System.out.println();
        System.out.println();

        Pizza nPizzaPextracchezz = new ExtracChezz(pizza);

        System.out.println(nPizzaPextracchezz.cost());
        System.out.println();
        System.out.println();

        Pizza extraSoucePizze = new ExtracSouce(nPizzaPextracchezz);
        System.out.println(extraSoucePizze.cost());




    }
}
