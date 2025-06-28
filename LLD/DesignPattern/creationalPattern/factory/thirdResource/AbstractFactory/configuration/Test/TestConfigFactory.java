package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.Test;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.ApiClient;
import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.ConfigurationFactory;
import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.DataBaseConnection;
import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.FeatureToggle;

public class TestConfigFactory implements ConfigurationFactory {

    @Override
    public DataBaseConnection createDbConnection() {
       return new TestDataBaseConnection();
    }

    @Override
    public ApiClient createApiClient() {
        return new TestApIClient();
    }

    @Override
    public FeatureToggle createFeature() {
       return new TestFeatureToggle();
    }
    
}
