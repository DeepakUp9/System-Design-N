package LLD.DesignPattern.creationalPattern.factory.thirdResource.factoryMethod.document;


class Excel implements Document{
    public boolean saveDocumentInDB(String filePath){
      System.out.println("saving Excel into saveDocumentInDB on this path: " + filePath);
      return true;
    }
}
 