package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.Prod;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.configuration.ApiClient;

public class ProdApIClient implements ApiClient{

    @Override
    public void getToken() {
       System.out.println("ProdApIClient to get the getToken");
    }
    
}
