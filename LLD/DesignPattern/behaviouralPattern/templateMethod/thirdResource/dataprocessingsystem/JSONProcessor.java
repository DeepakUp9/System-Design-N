package LLD.DesignPattern.behaviouralPattern.templateMethod.thirdResource.dataprocessingsystem;

public class JSONProcessor extends DataProcessing {

    @Override
    public void readData() {
        System.out.println("Reading the JSON data..");
    }

    @Override
    public void parseData() {
        System.out.println("parser the JSON data.");
    }

    @Override
    public void savingData() {
       System.out.println("Saving the JSON data into db.");
    }
}
