package LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.buildingtaxCalculation.Tax;

import LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.buildingtaxCalculation.TaxStrategy.TaxStrategy;

public class Tax {
    private TaxStrategy taxStrategy;

    public Tax(TaxStrategy taxStrategy){
        this.taxStrategy = taxStrategy;
    }

   
    public double taxCalculaton() {
       return taxStrategy.taxCalculaton();
    }

    public String taxPolicies() {
       return "Using tax...?  " + taxStrategy.taxPolicies();
    }


}
