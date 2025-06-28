package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration;

public interface ConfigurationFactory {
    DataBaseConnection createDbConnection();
    ApiClient createApiClient();
    FeatureToggle createFeature();
}
