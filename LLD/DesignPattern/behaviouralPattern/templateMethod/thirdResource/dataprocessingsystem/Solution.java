package LLD.DesignPattern.behaviouralPattern.templateMethod.thirdResource.dataprocessingsystem;

import javax.xml.crypto.Data;

public class Solution {
    public static void main(String[] args) {
        DataProcessing dataProcessing = new JSONProcessor();
        dataProcessing.processData();

        System.out.println();
        DataProcessing dataProcessing2 = new CSVProcessor();
        dataProcessing2.parseData();

        System.out.println();
        DataProcessing dataProcessing3 = new XMLProcessor();
        dataProcessing3.processData();
    }
}
