package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.Stage;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.DataBaseConnection;

public class StageDataBaseConnection implements DataBaseConnection{

    @Override
    public void connectDB() {
       System.out.println("StageDataBaseConnection connectDB");
    }
    
}
