package LLD.DesignPattern.creationalPattern.factory.thirdResource.factoryMethod.analyticsdashboardapplication;

import java.util.HashMap;
import java.util.Map;

public class PluginRegistry {

    private static final Map<String, ConnectorFactory> registry = new HashMap<>();

    public static void registerFactory(String type, ConnectorFactory factory) {
        registry.put(type.toLowerCase(), factory);
    }

    public static DataConnector getConnector(String type) {
        ConnectorFactory factory = registry.get(type.toLowerCase());
        if (factory == null) {
            throw new IllegalArgumentException("No connector registered for type: " + type);
        }
        return factory.createConnector();
    }
}
