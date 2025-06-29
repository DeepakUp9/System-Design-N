package LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.BufferingDecorator;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.FileReader.FileReader;

public class DoubleBuffering extends BufferingDecorator{

    public DoubleBuffering(FileReader fileReader) {
        super(fileReader);
    }

    @Override
    public void read() {
       System.out.println("DoubleBuffering");
    }
    
}

