package LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.Authentication;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.WebRequest.WebRequest;

public class JWTAuthentication extends AuthenticationDecorator{

    public JWTAuthentication(WebRequest webRequest) {
        super(webRequest);
    }

    @Override
    public String makePayment() {
       return super.makePayment()  + " JWTAuthentication";
    }
    
}
