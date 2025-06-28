package LLD.DesignPattern.creationalPattern.factory.thirdResource.factoryMethod.analyticsdashboardapplication;

class Solution {
   public static void main(String[] args) {
        try {
            // Load plugin classes to trigger static registration
            Class.forName("MySQLConnectorFactory");
            Class.forName("KafkaConnectorFactory");
            Class.forName("MongoDBConnectorFactory");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Dynamically get and use connectors
        DataConnector mysql = PluginRegistry.getConnector("mysql");
        mysql.connect();

        DataConnector kafka = PluginRegistry.getConnector("kafka");
        kafka.connect();

        DataConnector mongo = PluginRegistry.getConnector("mongodb");
        mongo.connect();
    }
}