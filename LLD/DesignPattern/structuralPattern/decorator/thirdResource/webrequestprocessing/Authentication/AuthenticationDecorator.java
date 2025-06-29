package LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.Authentication;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.WebRequestDecorator;
import LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.WebRequest.WebRequest;

public abstract class AuthenticationDecorator extends WebRequestDecorator{
    WebRequest webRequest;

    public AuthenticationDecorator(WebRequest webRequest){
        this.webRequest = webRequest;
    }

    @Override
    public String makePayment() {
       return webRequest.makePayment() + " AuthenticationDecorator";
    }

}
