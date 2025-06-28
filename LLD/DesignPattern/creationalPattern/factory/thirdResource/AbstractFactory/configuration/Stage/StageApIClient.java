package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.Stage;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.ApiClient;

public class StageApIClient implements ApiClient{

    @Override
    public void getToken() {
       System.out.println("StageApIClient to get the getToken");
    }
    
}
