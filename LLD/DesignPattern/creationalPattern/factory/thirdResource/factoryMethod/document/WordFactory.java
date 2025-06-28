package LLD.DesignPattern.creationalPattern.factory.thirdResource.factoryMethod.document;

class WordFactory extends DocumentFactory{
    
    public Document createDocument(){
        return new Word();
    }
}