package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.Test;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.DataBaseConnection;

public class TestDataBaseConnection implements DataBaseConnection{

    @Override
    public void connectDB() {
       System.out.println("TestDataBaseConnection connectDB");
    }
    
}
