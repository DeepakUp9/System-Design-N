package LLD.DesignPattern.structuralPattern.decorator.thirdResource.coffeeshopbillingsystem.Coffee;

public class Americano extends Coffee {

    @Override
    public double cost() {
       return 120;
    }

    @Override
    public String description() {
        return this.desription + " Americano";
    }
    
}
