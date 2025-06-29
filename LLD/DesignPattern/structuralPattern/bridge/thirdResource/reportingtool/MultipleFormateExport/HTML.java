package LLD.DesignPattern.structuralPattern.bridge.thirdResource.reportingtool.MultipleFormateExport;

public class HTML implements MultipleFormateExport{

    @Override
    public void fetch() {
        System.out.println("HTML formate data fetching");
    }
    
}
