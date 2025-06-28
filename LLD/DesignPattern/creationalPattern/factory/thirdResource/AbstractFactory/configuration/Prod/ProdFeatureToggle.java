package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.Prod;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.FeatureToggle;

public class ProdFeatureToggle implements FeatureToggle{

    @Override
    public void toggle() {
       System.out.println("ProdFeatureToggle for the toggle");
    }
    
}
