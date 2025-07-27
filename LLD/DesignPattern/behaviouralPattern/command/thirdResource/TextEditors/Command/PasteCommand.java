package LLD.DesignPattern.behaviouralPattern.command.thirdResource.TextEditors.Command;

import LLD.DesignPattern.behaviouralPattern.command.thirdResource.TextEditors.TextEditor;

public class PasteCommand implements Command {
    private final TextEditor editor;
    private final String text;

    public PasteCommand(TextEditor editor, String text) {
        this.editor = editor;
        this.text = text;
    }

    @Override
    public void execute() {
        editor.paste(text);
    }

    @Override
    public void undo() {
        editor.removeLast(text.length());
        System.out.println("Undo paste: removed '" + text + "'");
    }
}
