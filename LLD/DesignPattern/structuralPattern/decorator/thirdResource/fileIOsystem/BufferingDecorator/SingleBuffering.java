package LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.BufferingDecorator;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.FileReader.FileReader;

public class SingleBuffering extends BufferingDecorator{

    public SingleBuffering(FileReader fileReader) {
        super(fileReader);
    }

    @Override
    public void read() {
       System.out.println("SingleBuffering");
    }
    
}
