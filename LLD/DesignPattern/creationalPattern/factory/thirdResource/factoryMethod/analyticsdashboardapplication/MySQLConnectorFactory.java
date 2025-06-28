package LLD.DesignPattern.creationalPattern.factory.thirdResource.factoryMethod.analyticsdashboardapplication;

public class MySQLConnectorFactory implements ConnectorFactory {
    static {
        PluginRegistry.registerFactory("mysql", new MySQLConnectorFactory());
    }

    @Override
    public DataConnector createConnector() {
        return new MySQLConnector();
    }
}