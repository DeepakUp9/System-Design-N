package LLD.DesignPattern.structuralPattern.proxy.thirdResource.ProtectionProxy.documentmanagement;

public class DocumentDao {
    String name;
    String documenType;
    int pageCount;
    public void setName(String name) {
        this.name = name;
    }
    public void setDocumenType(String documenType) {
        this.documenType = documenType;
    }
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
    public String getName() {
        return name;
    }
    public String getDocumenType() {
        return documenType;
    }
    public int getPageCount() {
        return pageCount;
    }
    @Override
    public String toString() {
        return String.format("DocumentDao [name=%s, documenType=%s, pageCount=%s]", name, documenType, pageCount);
    }

    
    
}
