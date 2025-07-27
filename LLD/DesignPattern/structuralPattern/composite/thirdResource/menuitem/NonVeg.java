package LLD.DesignPattern.structuralPattern.composite.thirdResource.menuitem;

public class NonVeg implements MenuIteam {
    private String nvName;
    private double nvPrice;
    

    public NonVeg(String nvName, double nvPrice) {
        this.nvName = nvName;
        this.nvPrice = nvPrice;
    }

    @Override
    public double getPrice() {
       return this.nvPrice;
    }

    @Override
    public void printDescription() {
       System.out.println("This is the non-veg item " + nvName);
    }
}
