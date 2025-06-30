package LLD.DesignPattern.structuralPattern.decorator.secondResource.waffleDecorator.solution;

import LLD.DesignPattern.structuralPattern.decorator.secondResource.waffleDecorator.solution.BaseWaffle.IndianBase;
import LLD.DesignPattern.structuralPattern.decorator.secondResource.waffleDecorator.solution.Decorator.ChocolateDecorator.CaramelChocolate;
import LLD.DesignPattern.structuralPattern.decorator.secondResource.waffleDecorator.solution.Decorator.ChocolateDecorator.DarkChocolate;
import LLD.DesignPattern.structuralPattern.decorator.secondResource.waffleDecorator.solution.Decorator.FruitDecorator.AppleDecorator;
import LLD.DesignPattern.structuralPattern.decorator.secondResource.waffleDecorator.solution.Decorator.FruitDecorator.BananaDecorator;
import LLD.DesignPattern.structuralPattern.decorator.secondResource.waffleDecorator.solution.BaseWaffle.BaseWaffleClass;
public class solution {
    public static void main(String[] args) {
        BaseWaffleClass base = new IndianBase();
        System.out.println(base.getDescription() + " " + base.getPrice());

        CaramelChocolate caramel = new CaramelChocolate(base);
        System.out.println(caramel.getDescription() + " "+ caramel.getPrice());

        CaramelChocolate caramelDouble = new CaramelChocolate(caramel);
        System.out.println(caramelDouble.getDescription() + " " + caramelDouble.getPrice());

        DarkChocolate darkChocolate = new DarkChocolate(caramel);
        System.out.println(darkChocolate.getDescription() + " "+darkChocolate.getPrice());

        AppleDecorator appleDecorator = new AppleDecorator(darkChocolate);
        System.out.println(appleDecorator.getDescription() + " " + appleDecorator.getPrice());

        BananaDecorator bananaDecorator = new BananaDecorator(caramel);
        System.out.println(bananaDecorator.getDescription() + " " + bananaDecorator.getPrice());
    }
}
