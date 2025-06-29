package LLD.DesignPattern.structuralPattern.proxy.thirdResource.ProtectionProxy.documentmanagement;

public interface Document {
    void createDoc(String user, DocumentDao dao);
    void deleteDoc(String user, DocumentDao dao); 
    void showDoc(String user);
}
