package LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.EncryptionDecorator;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.FileReader.FileReader;

public class SymmetricEncryption extends EncryptionDecorator{

    public SymmetricEncryption(FileReader fileReader) {
        super(fileReader);
    }

    @Override
    public void read() {
       System.out.println("SymmetricEncryption");
    }
    
}
