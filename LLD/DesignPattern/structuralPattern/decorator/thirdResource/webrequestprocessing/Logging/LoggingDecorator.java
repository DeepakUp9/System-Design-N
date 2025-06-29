package LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.Logging;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.WebRequestDecorator;
import LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.WebRequest.WebRequest;

public abstract class LoggingDecorator extends WebRequestDecorator{
    WebRequest webRequest;

    public LoggingDecorator(WebRequest webRequest){
        this.webRequest = webRequest;
    }

    @Override
    public String makePayment() {
       return webRequest.makePayment() + " LoggingDecorator";
    }

}
