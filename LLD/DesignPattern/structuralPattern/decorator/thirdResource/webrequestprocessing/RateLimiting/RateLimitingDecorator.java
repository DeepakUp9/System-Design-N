package LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.RateLimiting;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.WebRequestDecorator;
import LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.WebRequest.WebRequest;

public abstract class RateLimitingDecorator extends WebRequestDecorator{
    WebRequest webRequest;

    public RateLimitingDecorator(WebRequest webRequest){
        this.webRequest = webRequest;
    }

    @Override
    public String makePayment() {
       return webRequest.makePayment() + " RateLimitingDecorator";
    }

}