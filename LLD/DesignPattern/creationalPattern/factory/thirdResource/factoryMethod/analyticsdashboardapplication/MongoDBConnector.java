package LLD.DesignPattern.creationalPattern.factory.thirdResource.factoryMethod.analyticsdashboardapplication;


public class MongoDBConnector implements DataConnector {
    @Override
    public void connect() {
        System.out.println("Connected to MongoDB database");
    }
}
