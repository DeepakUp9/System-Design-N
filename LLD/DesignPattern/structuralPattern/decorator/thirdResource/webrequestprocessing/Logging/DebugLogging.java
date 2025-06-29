package LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.Logging;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.WebRequest.WebRequest;

public class DebugLogging extends LoggingDecorator{

    public DebugLogging(WebRequest webRequest) {
        super(webRequest);
    }

    @Override
    public String makePayment() {
       return super.makePayment()  + " DebugLogging";
    }

}
