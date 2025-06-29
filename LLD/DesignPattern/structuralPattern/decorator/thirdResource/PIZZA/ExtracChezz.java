package LLD.DesignPattern.structuralPattern.decorator.thirdResource.PIZZA;

public class ExtracChezz extends DecoratorPizza {

    Pizza pizza;
    ExtracChezz(Pizza pizza ){
      this.pizza = pizza;
    }

    @Override
    int cost() {
       return this.pizza.cost() + 20;
    }
    
}
