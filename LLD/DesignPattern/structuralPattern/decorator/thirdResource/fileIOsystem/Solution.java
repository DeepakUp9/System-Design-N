package LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem;

import LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.BufferingDecorator.DoubleBuffering;
import LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.CompressionDecorator.RARCompression;
import LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.CompressionDecorator.ZIPCompression;
import LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.EncryptionDecorator.ASymmetricEncryption;
import LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.FileReader.BinaryData;
import LLD.DesignPattern.structuralPattern.decorator.thirdResource.fileIOsystem.FileReader.FileReader;

public class Solution {
    public static void main(String[] args) {
        FileReader fileReader = new BinaryData();
        fileReader.read();

        FileReader dFileReader = new DoubleBuffering(fileReader);
        dFileReader.read();

        FileReader zFileReader =new ZIPCompression(dFileReader);
        zFileReader.read();

        FileReader asFileReader = new ASymmetricEncryption(zFileReader);
        asFileReader.read();

        FileReader aFileReader =new RARCompression(asFileReader);
        aFileReader.read();


    }
}
