package LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.buildingtaxCalculation.Tax;

import LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.buildingtaxCalculation.TaxStrategy.TaxStrategy;

public class USA extends Tax{

    public USA(TaxStrategy taxStrategy) {
        super(taxStrategy);
    }
    
}
