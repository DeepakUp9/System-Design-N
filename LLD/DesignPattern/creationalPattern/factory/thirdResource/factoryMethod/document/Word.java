package LLD.DesignPattern.creationalPattern.factory.thirdResource.factoryMethod.document;

class Word implements Document{
   public boolean saveDocumentInDB(String filePath){
     System.out.println("saving Word into saveDocumentInDB on this path: "+ filePath);
     return true;
   }
}
