package LLD.DesignPattern.structuralPattern.adapter.thirdResource.documentConversiontool;

import LLD.DesignPattern.structuralPattern.adapter.thirdResource.documentConversiontool.ObjectAdapter.Document;
import LLD.DesignPattern.structuralPattern.adapter.thirdResource.documentConversiontool.ObjectAdapter.PdfDocumentAdapter;
import LLD.DesignPattern.structuralPattern.adapter.thirdResource.documentConversiontool.ObjectAdapter.PdfLibrary;

public class Solution {
    public static void main(String[] args) {
        // Using Object Adapter
        PdfLibrary thirdPartyPdfLib = new PdfLibrary();
        Document doc = new PdfDocumentAdapter(thirdPartyPdfLib);


        doc.open();
        doc.setContent("Hello World");
        System.out.println("Content: " + doc.getContent());
        doc.save();

    }
}
