package designPattern.StateDesignPattern.DocumentEditor;

public class Solution {
    public static void main(String[] args) {
        DocumentEditor documentEditor = new DocumentEditor();

        documentEditor.edit();
        documentEditor.comment();
        documentEditor.publish();

        documentEditor.edit();
        documentEditor.comment();
        documentEditor.publish();

        documentEditor.edit();
        documentEditor.comment();
        documentEditor.publish();

    }
}
