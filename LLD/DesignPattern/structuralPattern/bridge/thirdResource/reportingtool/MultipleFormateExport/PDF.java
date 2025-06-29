package LLD.DesignPattern.structuralPattern.bridge.thirdResource.reportingtool.MultipleFormateExport;

public class PDF implements MultipleFormateExport {

    @Override
    public void fetch() {
       System.out.println("PDF formate data fetching");
    }
    
}
