package LLD.DesignPattern.structuralPattern.proxy.thirdResource.ProtectionProxy.documentmanagement;

public class ProxyDocument implements Document{
    Document document;

    ProxyDocument(){
        document = new RealDocument();
    }

    @Override
    public void createDoc(String user, DocumentDao dao) {
        if(user.equals("Admin")){
            document.createDoc(user, dao);
        }else{
            System.out.println("Denied User, this User can't create the doc");
        }
    }

    @Override
    public void deleteDoc(String user, DocumentDao dao) {
        if(user.equals("Admin")){
            document.deleteDoc(user, dao);
        }else{
            System.out.println("Denied User, this User can't delete the doc");
        }
    }

    @Override
    public void showDoc(String user) {
        if(user.equals("Admin") || user.equals("User")){
            document.showDoc(user);
        }else{
            System.out.println("Invalid User can't access the document");
        }
    }
    
}
