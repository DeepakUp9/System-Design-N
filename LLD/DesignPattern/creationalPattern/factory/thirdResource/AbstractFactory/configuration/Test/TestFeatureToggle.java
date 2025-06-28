package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.Test;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.FeatureToggle;

public class TestFeatureToggle implements FeatureToggle{

    @Override
    public void toggle() {
       System.out.println("TestFeatureToggle for the toggle");
    }
    
}
