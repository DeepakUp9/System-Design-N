package LLD.DesignPattern.structuralPattern.composite.thirdResource.menuitem;

public class Solution {
    public static void main(String[] args) {
        MenuIteam vegP = new Veg("palak Panner ", 100);
        MenuIteam vegs = new Veg("Sabji", 90);

        MenuIteam chicken = new Veg("Chicken ", 300);
        MenuIteam popCorn = new Veg("popCornChicken", 90);

        Combo combo = new Combo("Veg + NonVeg Combo", 100);

        combo.add(vegP);
        combo.add(chicken);

        combo.printDescription();
        System.out.println(combo.getPrice());

    }
}
