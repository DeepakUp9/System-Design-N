package LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.CompressionDecorator;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.FileReader.FileReader;

public class RARCompression extends CompressionDecorator{

    public RARCompression(FileReader fileReader) {
        super(fileReader);
    }

    @Override
    public void read() {
        System.out.println("RARCompression");
    }
    
}