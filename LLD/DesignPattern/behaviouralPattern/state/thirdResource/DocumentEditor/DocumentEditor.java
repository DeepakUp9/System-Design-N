package LLD.DesignPattern.behaviouralPattern.state.thirdResource.DocumentEditor;

public class DocumentEditor {

    DocumentState documentState;

    public DocumentEditor() {
        this.documentState = new Draft();
    }

    public DocumentState getDocumentState() {
        return documentState;
    }

    public void setDocumentState(DocumentState documentState) {
        this.documentState = documentState;
    }

    public void edit() {
        documentState.edit(this);
    }

    public void comment() {
        documentState.comment(this);
    }

    public void publish() {
        documentState.publish(this);
    }

}
