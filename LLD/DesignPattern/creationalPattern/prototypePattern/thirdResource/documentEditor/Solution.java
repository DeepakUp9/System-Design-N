package LLD.DesignPattern.creationalPattern.prototypePattern.thirdResource.documentEditor;


import java.util.List;

public class Solution {
    public static void main(String[] args) throws CloneNotSupportedException {
        DocumentEditor documentEditor = new DocumentEditor();
        documentEditor.setFooter("footer");
        documentEditor.setContent("content");
        documentEditor.setHeader("header");
        documentEditor.setUserAccess(List.of("A", "B", "C"));

        DocumentEditor documentEditor2 = (DocumentEditor) documentEditor.clone();
        System.out.println(documentEditor2);
        System.out.println(documentEditor);
    }
}
