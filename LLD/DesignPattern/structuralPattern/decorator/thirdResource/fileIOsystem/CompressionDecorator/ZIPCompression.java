package LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.CompressionDecorator;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.FileReader.FileReader;

public class ZIPCompression extends CompressionDecorator{

    public ZIPCompression(FileReader fileReader) {
        super(fileReader);
    }

    @Override
    public void read() {
        System.out.println("ZIPCompression");
    }
    
}
