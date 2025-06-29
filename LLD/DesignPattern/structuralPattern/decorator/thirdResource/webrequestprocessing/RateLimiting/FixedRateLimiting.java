package LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.RateLimiting;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.WebRequest.WebRequest;

public class FixedRateLimiting extends RateLimitingDecorator {

    public FixedRateLimiting(WebRequest webRequest) {
        super(webRequest);
    }

    @Override
    public String makePayment() {
       return super.makePayment()  + " FixedRateLimiting";
    }
    
}
