package LLD.DesignPattern.behaviouralPattern.templateMethod.thirdResource.dataprocessingsystem;

public class XMLProcessor extends DataProcessing{
    
     @Override
    public void readData() {
        System.out.println("Reading the XML data..");
    }

    @Override
    public void parseData() {
        System.out.println("parser the XML data.");
    }

    @Override
    public void savingData() {
       System.out.println("Saving the XML data into db.");
    }
}
