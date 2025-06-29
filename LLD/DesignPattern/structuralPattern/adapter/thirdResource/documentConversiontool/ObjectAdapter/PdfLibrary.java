package LLD.DesignPattern.structuralPattern.adapter.thirdResource.documentConversiontool.ObjectAdapter;

// Adaptee (Existing PDF Library - Incompatible Interface)
public class PdfLibrary {
    public void pdfOpen() {
        System.out.println("PDF: Opening document");
    }
    
    public void pdfSave() {
        System.out.println("PDF: Saving document");
    }
    
    public String fetchPdfContent() {
        return "PDF Content";
    }
    
    public void updatePdfContent(String content) {
        System.out.println("PDF: Updating content to: " + content);
    }
}