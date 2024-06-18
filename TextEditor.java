import java.util.*;

public class TextEditor {
    StringBuilder text;
    Stack<String> undoStack;
    Stack<String> redoStack;

    public TextEditor() {
        text = new StringBuilder();
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void addText(String newText) {
        undoStack.push(text.toString());
        text.append(newText);
        redoStack.clear();
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(text.toString());
            text = new StringBuilder(undoStack.pop());
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(text.toString());
            text = new StringBuilder(redoStack.pop());
        }
    }

    public void deleteWord(String wordToDelete) {
        String txt = text.toString();
        txt = txt.replace(wordToDelete, "");
        undoStack.push(text.toString());
        text = new StringBuilder(txt);
        redoStack.clear();
    }

    public String getText() {
        return text.toString();
    }

    public boolean findWord(String wordToFind) {
        String txt = text.toString();
        return txt.contains(wordToFind);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TextEditor editor = new TextEditor();
    }
}
