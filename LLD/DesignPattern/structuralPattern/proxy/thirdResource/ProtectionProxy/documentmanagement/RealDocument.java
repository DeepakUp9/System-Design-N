package LLD.DesignPattern.structuralPattern.proxy.thirdResource.ProtectionProxy.documentmanagement;

public class RealDocument implements Document  {

    DocumentDao documentDao;
    RealDocument(){
        documentDao = new DocumentDao();
    }

    @Override
    public void createDoc(String user, DocumentDao dao) {
       documentDao.setName(dao.name);
       documentDao.setDocumenType(dao.documenType);
       documentDao.setPageCount(dao.pageCount);
    }

    @Override
    public void deleteDoc(String user, DocumentDao dao) {
        if(user.equals("Admin")){
            System.out.println("User deleted SuccessFully");
        }
    }

    @Override
    public void showDoc(String user) {
        System.out.println("User SuccessFully display");
    }

}
