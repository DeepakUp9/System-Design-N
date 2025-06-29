package LLD.DesignPattern.structuralPattern.adapter.thirdResource.documentConversiontool.ObjectAdapter;

// Object Adapter (Wraps the Adaptee)
public class PdfDocumentAdapter implements Document {
    private PdfLibrary pdfLibrary;  // Composition
    
    public PdfDocumentAdapter(PdfLibrary pdfLibrary) {
        this.pdfLibrary = pdfLibrary;
    }
    
    @Override
    public void open() {
        pdfLibrary.pdfOpen();  // Delegates to Adaptee
    }
    
    @Override
    public void save() {
        pdfLibrary.pdfSave();  // Delegates to Adaptee
    }
    
    @Override
    public String getContent() {
        return pdfLibrary.fetchPdfContent();  // Adapts method name
    }
    
    @Override
    public void setContent(String content) {
        pdfLibrary.updatePdfContent(content);  // Adapts method name
    }
}