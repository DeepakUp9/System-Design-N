package LLD.DesignPattern.behaviouralPattern.command.thirdResource.TextEditors;

public class TextEditor {
    private StringBuilder content = new StringBuilder();

    public void copy(String text) {
        System.out.println("Copied: " + text);
    }

    public void paste(String text) {
        content.append(text);
        System.out.println("Pasted: " + text);
    }

    public void removeLast(int length) {
        if (length <= content.length()) {
            content.delete(content.length() - length, content.length());
        }
    }

    public String getContent() {
        return content.toString();
    }
}
