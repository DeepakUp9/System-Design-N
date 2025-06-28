package LLD.DesignPattern.creationalPattern.factory.thirdResource.factoryMethod.document;


class PDF implements Document{
    public boolean saveDocumentInDB(String filePath){
      System.out.println("saving PDF into saveDocumentInDB on this path: "+ filePath);
      return true;
    }
 }
 