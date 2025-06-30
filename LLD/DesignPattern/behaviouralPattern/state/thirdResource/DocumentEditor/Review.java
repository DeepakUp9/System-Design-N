package designPattern.StateDesignPattern.DocumentEditor;

public class Review implements DocumentState {

    @Override
    public void edit(DocumentEditor documentEditor) {
        System.out.println(" reviewing document");

    }

    @Override
    public void comment(DocumentEditor documentEditor) {
        System.out.println(" adding comments in review state");
    }

    @Override
    public void publish(DocumentEditor documentEditor) {
        System.out.println(" review is finished ");
        documentEditor.setDocumentState(new Published());
    }

}
