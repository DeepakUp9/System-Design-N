package LLD.DesignPattern.behaviouralPattern.state.thirdResource.DocumentEditor;

public class Draft implements DocumentState {

    public void edit(DocumentEditor documentEditor) {
        System.out.println(" editing draft");

    }

    @Override
    public void comment(DocumentEditor documentEditor) {
        System.out.println(" adding comments in draft");

    }

    @Override
    public void publish(DocumentEditor documentEditor) {
        System.out.println(" drafting is completed");
        documentEditor.setDocumentState(new Review());

    }

}
