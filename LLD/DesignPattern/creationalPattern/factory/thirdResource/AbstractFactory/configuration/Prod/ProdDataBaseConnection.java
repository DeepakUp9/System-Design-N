package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.Prod;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.DataBaseConnection;

public class ProdDataBaseConnection implements DataBaseConnection{

    @Override
    public void connectDB() {
       System.out.println("ProdDataBaseConnection connectDB");
    }
    
}
