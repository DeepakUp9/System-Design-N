package LLD.DesignPattern.structuralPattern.decorator.thirdResource.webrequestprocessing.WebRequest;

public class GetRequest extends WebRequest {

    @Override
    public String makePayment() {
       return "Using GetRequest MakePayment";
    }
    
}
