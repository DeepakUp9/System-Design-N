package LLD.DesignPattern.creationalPattern.factory.thirdResource.factoryMethod.document;

abstract class DocumentFactory{
    abstract Document createDocument();

    public void printDocumentFactory(){
        System.out.print("printing DocumentFactory....");
    }
    
}