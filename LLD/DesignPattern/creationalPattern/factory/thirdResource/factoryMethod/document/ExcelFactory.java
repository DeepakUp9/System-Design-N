package LLD.DesignPattern.creationalPattern.factory.thirdResource.factoryMethod.document;

class ExcelFactory extends DocumentFactory{
    public Document createDocument(){
        return new Excel();
    }
}