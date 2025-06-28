package LLD.DesignPattern.creationalPattern.factory.thirdResource.factoryMethod.document;


class Solution {
    public static void main(String []args){
        DocumentFactory wordFactory = new WordFactory();
        Document word = wordFactory.createDocument();
        word.saveDocumentInDB("from google drive");

        DocumentFactory exceltFactory = new ExcelFactory();
        Document excel = exceltFactory.createDocument();
        excel.saveDocumentInDB("from microsoft Excel");
    }
}