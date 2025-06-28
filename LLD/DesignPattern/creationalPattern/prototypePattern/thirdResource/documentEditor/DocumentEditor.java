package LLD.DesignPattern.creationalPattern.prototypePattern.thirdResource.documentEditor;

import java.util.List;

public class DocumentEditor implements Cloneable{

    private String content;
    private String header;
    private String footer;
    private List<String>userAccess;
    public void setContent(String content) {
        this.content = content;
    }
    public void setHeader(String header) {
        this.header = header;
    }
    public void setFooter(String footer) {
        this.footer = footer;
    }
    public void setUserAccess(List<String> userAccess) {
        this.userAccess = userAccess;
    }
    public String getContent() {
        return content;
    }
    public String getHeader() {
        return header;
    }
    public String getFooter() {
        return footer;
    }
    public List<String> getUserAccess() {
        return userAccess;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
    @Override
    public String toString() {
        return String.format("DocumentEditor [content=%s, header=%s, footer=%s, userAccess=%s]", content, header, footer, userAccess);
    }

    

    
}