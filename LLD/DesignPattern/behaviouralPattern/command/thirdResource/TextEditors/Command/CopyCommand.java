package LLD.DesignPattern.behaviouralPattern.command.thirdResource.TextEditors.Command;

import LLD.DesignPattern.behaviouralPattern.command.thirdResource.TextEditors.TextEditor;

public class CopyCommand implements Command {
    private final TextEditor editor;
    private final String text;

    public CopyCommand(TextEditor editor, String text) {
        this.editor = editor;
        this.text = text;
    }

    @Override
    public void execute() {
        editor.copy(text);
    }

    @Override
    public void undo() {
        System.out.println("Undo not applicable for copy.");
    }
}
