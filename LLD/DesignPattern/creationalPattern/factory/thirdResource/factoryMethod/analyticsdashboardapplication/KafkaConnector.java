package LLD.DesignPattern.creationalPattern.factory.thirdResource.factoryMethod.analyticsdashboardapplication;

public class KafkaConnector implements DataConnector {
    @Override
    public void connect() {
        System.out.println("Connected to Kafka stream");
    }
}
