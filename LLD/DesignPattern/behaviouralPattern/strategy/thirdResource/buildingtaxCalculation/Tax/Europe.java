package LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.buildingtaxCalculation.Tax;

import LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.buildingtaxCalculation.TaxStrategy.TaxStrategy;

public class Europe extends Tax{

    public Europe(TaxStrategy taxStrategy) {
        super(taxStrategy);
    }
    
}
