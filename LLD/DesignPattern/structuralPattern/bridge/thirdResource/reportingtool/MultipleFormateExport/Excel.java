package LLD.DesignPattern.structuralPattern.bridge.thirdResource.reportingtool.MultipleFormateExport;

public class Excel implements MultipleFormateExport{

    @Override
    public void fetch() {
       System.out.println("Excel formate data fetching");
    }
    
}
