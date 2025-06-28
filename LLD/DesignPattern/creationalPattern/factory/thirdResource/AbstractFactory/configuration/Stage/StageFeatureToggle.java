package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.Stage;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.FeatureToggle;

public class StageFeatureToggle implements FeatureToggle{

    @Override
    public void toggle() {
       System.out.println("StageFeatureToggle for the toggle");
    }
    
}
