package LLD.DesignPattern.behaviouralPattern.state.thirdResource.DocumentEditor;

public class Published implements DocumentState {

    @Override
    public void edit(DocumentEditor documentEditor) {
        System.out.println(" In publishing state");

    }

    @Override
    public void comment(DocumentEditor documentEditor) {
        System.out.println(" adding comments in published state");

    }

    @Override
    public void publish(DocumentEditor documentEditor) {
        System.out.println(" document is published ");

    }

}
