package LLD.DesignPattern.structuralPattern.decorator.thirdResource.PIZZA;

public class ExtracSouce extends DecoratorPizza {

    Pizza pizza; // has-relation
    ExtracSouce ( Pizza pizza ){
      this.pizza = pizza;
    }
    
    @Override
    int cost() {
      return this.pizza.cost() + 5;
    }
    
}
