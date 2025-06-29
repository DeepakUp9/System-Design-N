package LLD.DesignPattern.structuralPattern.adapter.thirdResource.documentConversiontool.ObjectAdapter;

// Target Interface (What the Client expects)
public interface Document {
    void open();
    void save();
    String getContent();
    void setContent(String content);
}
