package LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.Authentication;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.WebRequest.WebRequest;

public class CertificateAuthentication extends AuthenticationDecorator{

    public CertificateAuthentication(WebRequest webRequest) {
        super(webRequest);
    }

    @Override
    public String makePayment() {
       return super.makePayment()  +" CertificateAuthentication";
    }
    
}