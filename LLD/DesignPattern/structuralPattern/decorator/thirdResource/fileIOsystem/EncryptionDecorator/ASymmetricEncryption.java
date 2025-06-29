package LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.EncryptionDecorator;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.FileReader.FileReader;

public class ASymmetricEncryption extends EncryptionDecorator{

    public ASymmetricEncryption(FileReader fileReader) {
        super(fileReader);
    }

    @Override
    public void read() {
       System.out.println("ASymmetricEncryption");
    }
    
}

