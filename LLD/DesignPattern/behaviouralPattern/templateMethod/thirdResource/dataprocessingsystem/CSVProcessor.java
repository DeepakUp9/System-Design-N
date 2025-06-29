package LLD.DesignPattern.behaviouralPattern.templateMethod.thirdResource.dataprocessingsystem;

public class CSVProcessor extends DataProcessing{

    @Override
    public void readData() {
        System.out.println("Reading the CSV data..");
    }

    @Override
    public void parseData() {
        System.out.println("parser the CSV data.");
    }

    @Override
    public void savingData() {
       System.out.println("Saving the CSV data into db.");
    }
    
}
