package LLD.DesignPattern.structuralPattern.proxy.thirdResource.ProtectionProxy.documentmanagement;

public class Solution {
    public static void main(String[] args) {
        Document document = new ProxyDocument();
        DocumentDao dao = new DocumentDao();
        dao.setDocumenType("TaxSheet");
        dao.setPageCount(10);
        dao.setName("Tax");

        document.createDoc("Admins", dao);
        
    }
}
