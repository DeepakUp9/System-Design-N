package LLD.DesignPattern.structuralPattern.composite.thirdResource.menuitem;

import java.util.ArrayList;
import java.util.List;

public class Combo implements MenuIteam{
    private String cName;
    private double cPrice; 
    private List<MenuIteam>items;
    

    public Combo(String cName, double cPrice) {
        this.cName = cName;
        this.cPrice = cPrice;
        items = new ArrayList<>();
    }

    public void add(MenuIteam menuIteam){
        items.add(menuIteam);
    }
    public void remove(MenuIteam menuIteam){
        items.remove(menuIteam);
    }

    @Override
    public double getPrice() {
       double totalPrice = 0;
       for(MenuIteam item: items){
            totalPrice += item.getPrice();
       } 
       return totalPrice - cPrice;
    }

    @Override
    public void printDescription() {
        System.out.println("This is Combo" + cName);
        for(MenuIteam item: items){
            item.printDescription();;
        } 
    }
}
