package LLD.DesignPattern.behaviouralPattern.command.thirdResource.TextEditors;

import java.util.Stack;

import LLD.DesignPattern.behaviouralPattern.command.thirdResource.TextEditors.Command.Command;

public class CommandManager {
    private final Stack<Command> history = new Stack<>();

    public void executeCommand(Command command) {
        command.execute();
        history.push(command);
    }

    public void undo() {
        if (!history.isEmpty()) {
            Command last = history.pop();
            last.undo();
        }
    }
}