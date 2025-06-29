package testLLCode.Decoratorpattern.coffeeshopbillingsystem.SugarDecorator;

import testLLCode.Decoratorpattern.coffeeshopbillingsystem.Coffee.Coffee;

public class SugarFree extends SugarDecorator{

    public SugarFree(Coffee coffee) {
        super(coffee);
    }

     public double cost() {
        return coffee.cost() + 5;
    }

    public String description() {
      return coffee.description() + "SugarFree";
    }
    
    
}
