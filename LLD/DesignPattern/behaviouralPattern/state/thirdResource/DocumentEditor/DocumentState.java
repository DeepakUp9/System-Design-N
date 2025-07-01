package LLD.DesignPattern.behaviouralPattern.state.thirdResource.DocumentEditor;

public interface DocumentState {

    public void edit(DocumentEditor documentEditor);

    public void comment(DocumentEditor documentEditor);

    public void publish(DocumentEditor documentEditor);

}
