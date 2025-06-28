package LLD.DesignPattern.creationalPattern.factory.thirdResource.factoryMethod.analyticsdashboardapplication;


public class MongoDBConnectorFactory implements ConnectorFactory {

    // This static block registers the plugin automatically at runtime
    static {
        PluginRegistry.registerFactory("mongodb", new MongoDBConnectorFactory());
    }

    @Override
    public DataConnector createConnector() {
        return new MongoDBConnector();
    }
}
