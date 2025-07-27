package LLD.DesignPattern.behaviouralPattern.command.thirdResource.TextEditors;

import LLD.DesignPattern.behaviouralPattern.command.thirdResource.TextEditors.Command.Command;
import LLD.DesignPattern.behaviouralPattern.command.thirdResource.TextEditors.Command.CopyCommand;
import LLD.DesignPattern.behaviouralPattern.command.thirdResource.TextEditors.Command.PasteCommand;

public class Solution {
     public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        CommandManager manager = new CommandManager();

        Command copy = new CopyCommand(editor, "Hello");
        Command paste = new PasteCommand(editor, " World");

        manager.executeCommand(copy);
        manager.executeCommand(paste);

        System.out.println("Current content: " + editor.getContent());

        manager.undo(); // undo paste
        System.out.println("After undo: " + editor.getContent());
    }
}
