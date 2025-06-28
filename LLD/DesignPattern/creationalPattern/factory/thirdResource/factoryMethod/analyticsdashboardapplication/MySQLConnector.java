package LLD.DesignPattern.creationalPattern.factory.thirdResource.factoryMethod.analyticsdashboardapplication;


public class MySQLConnector implements DataConnector {
    @Override
    public void connect() {
        System.out.println("Connected to MySQL database");
    }
}
