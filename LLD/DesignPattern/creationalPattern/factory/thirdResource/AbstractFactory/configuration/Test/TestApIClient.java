package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.Test;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.ApiClient;

public class TestApIClient implements ApiClient{

    @Override
    public void getToken() {
       System.out.println("TestApIClient to get the getToken");
    }
    
}
