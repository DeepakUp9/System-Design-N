package LLD.DesignPattern.creationalPattern.factory.thirdResource.factoryMethod.document;

class PDFFactory extends DocumentFactory{
    
    public Document createDocument(){
        return new PDF();
    }
}