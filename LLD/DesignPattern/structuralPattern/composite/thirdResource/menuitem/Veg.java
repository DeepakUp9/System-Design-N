package LLD.DesignPattern.structuralPattern.composite.thirdResource.menuitem;

public class Veg implements MenuIteam {
    private String vName;
    private double vPrice;
    

    public Veg(String vName, double vPrice) {
        this.vName = vName;
        this.vPrice = vPrice;
    }

    @Override
    public double getPrice() {
       return this.vPrice;
    }

    @Override
    public void printDescription() {
       System.out.println("This is the veg item " + vName);
    }
    
}
