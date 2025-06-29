package LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.CompressionDecorator;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.FileReaderDecorator;
import LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.FileReader.FileReader;

public abstract class CompressionDecorator  extends FileReaderDecorator {
    FileReader fileReader;
    public CompressionDecorator(FileReader fileReader){
        this.fileReader = fileReader;
    }

}