package LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.buildingtaxCalculation.TaxStrategy;

public class EUROPETaxStrategy implements TaxStrategy  {

    @Override
    public double taxCalculaton() {
       return Math.random();
    }

    @Override
    public String taxPolicies() {
       return "Europe tax taxPolicies";
    }
    
}
