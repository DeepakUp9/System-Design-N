package LLD.DesignPattern.creationalPattern.factory.thirdResource.factoryMethod.analyticsdashboardapplication;


public class KafkaConnectorFactory implements ConnectorFactory {
    static {
        PluginRegistry.registerFactory("kafka", new KafkaConnectorFactory());
    }

    @Override
    public DataConnector createConnector() {
        return new KafkaConnector();
    }
}