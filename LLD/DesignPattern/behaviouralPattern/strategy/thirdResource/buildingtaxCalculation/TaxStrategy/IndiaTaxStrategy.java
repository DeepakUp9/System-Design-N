package LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.buildingtaxCalculation.TaxStrategy;

public class IndiaTaxStrategy implements TaxStrategy{

    @Override
    public double taxCalculaton() {
       return Math.random();
    }

    @Override
    public String taxPolicies() {
       return "Indian tax policies";
    }
    
}
