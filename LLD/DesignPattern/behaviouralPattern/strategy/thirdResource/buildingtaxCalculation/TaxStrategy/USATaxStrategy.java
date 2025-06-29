package LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.buildingtaxCalculation.TaxStrategy;

public class USATaxStrategy implements TaxStrategy{

    @Override
    public double taxCalculaton() {
       return Math.random();
    }

    @Override
    public String taxPolicies() {
       return "USA tax policies";
    }
    
}
