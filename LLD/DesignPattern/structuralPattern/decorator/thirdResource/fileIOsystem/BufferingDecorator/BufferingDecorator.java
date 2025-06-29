package LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.BufferingDecorator;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.FileReaderDecorator;
import LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.FileReader.FileReader;

public abstract class BufferingDecorator extends FileReaderDecorator {
    FileReader fileReader;
    public BufferingDecorator(FileReader fileReader){
        this.fileReader = fileReader;
    }
}
