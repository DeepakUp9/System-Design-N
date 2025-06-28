package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.Stage;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.ApiClient;
import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.ConfigurationFactory;
import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.DataBaseConnection;
import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.FeatureToggle;

public class StageConfigFactory implements ConfigurationFactory{

    @Override
    public DataBaseConnection createDbConnection() {
       return new StageDataBaseConnection();
    }

    @Override
    public ApiClient createApiClient() {
        return new StageApIClient();
    }

    @Override
    public FeatureToggle createFeature() {
        return new StageFeatureToggle();
    }
    
}
