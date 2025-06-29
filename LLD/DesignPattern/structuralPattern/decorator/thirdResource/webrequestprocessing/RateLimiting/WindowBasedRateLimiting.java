package LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.RateLimiting;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.WebRequest.WebRequest;

public class WindowBasedRateLimiting extends RateLimitingDecorator {

    public WindowBasedRateLimiting(WebRequest webRequest) {
        super(webRequest);
    }

    @Override
    public String makePayment() {
       return super.makePayment()  + " WindowBasedRateLimiting";
    }
    
}