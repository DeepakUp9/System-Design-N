package LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.EncryptionDecorator;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.FileReaderDecorator;
import LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.FileReader.FileReader;

public abstract class EncryptionDecorator extends FileReaderDecorator {
    FileReader fileReader;
    public EncryptionDecorator(FileReader fileReader){
        this.fileReader = fileReader;
    }
}