package LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.FileReader;

public class PlainTextData extends FileReader{

    @Override
    public void read() {
       System.out.println("Reading the PlainTextData");
    }
    
}
