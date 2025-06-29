package LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.WebRequest;

public class PostRequest extends WebRequest {

    @Override
    public String makePayment() {
       return "Using PostRequest MakePayment";
    }
    
}
