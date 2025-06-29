package LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.Logging;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.WebRequest.WebRequest;

public class InfoLogging extends LoggingDecorator{

    public InfoLogging(WebRequest webRequest) {
        super(webRequest);
    }

    @Override
    public String makePayment() {
       return super.makePayment()  + " InfoLogging";
    }

}
