package LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.buildingtaxCalculation.Tax;

import LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.buildingtaxCalculation.TaxStrategy.TaxStrategy;

public class India extends Tax {

    public India(TaxStrategy taxStrategy) {
        super(taxStrategy);
    }
    
}
