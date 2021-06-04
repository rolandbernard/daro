package daro.ide.editor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import daro.lang.ast.Position;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.stage.FileChooser;

public class EditorTab extends Tab {
    private Path file;
    private TextEditor editor;
    private boolean unsaved;

    public EditorTab(String content) {
        this.file = null;
        this.editor = new TextEditor(content);
        init();
    }

    public EditorTab(Path file) {
        this.file = file;
        String content;
        try {
            content = Files.readString(file);
        } catch (IOException e) {
            content = "";
        }
        if (file.toString().endsWith(".daro")) {
            this.editor = new CodeEditor(content);
        } else {
            this.editor = new TextEditor(content);
        }
        init();
    }

    private void updateTitle() {
        String title;
        if (file != null) {
            title = file.getFileName().toString();
        } else {
            title = "[No Name]";
        }
        if (unsaved) {
            title += " +";
        }
        setText(title);
    }

    private void init() {
        setContent(editor);
        editor.setOnChange(value -> {
            unsaved = true;
            updateTitle();
        });
        updateTitle();
        setOnCloseRequest(event -> {
            if (unsaved) {
                ConfirmDialog alert = new ConfirmDialog("Close without saving?");
                if (alert.showAndWait().orElse(null) != ButtonType.OK) {
                    event.consume();
                }
            }
        });
    }

    public Path getFile() {
        return file;
    }

    public boolean isUnsaved() {
        return unsaved;
    }

    public String getEditorContent() {
        return editor.getText();
    }

    public void highlightError(Position position) {
        editor.highlightError(position);
    }

    public void saveFile() {
        if (file != null) {
            try {
                Files.writeString(file, editor.getText());
                unsaved = false;
                updateTitle();
            } catch (IOException e) {}
        } else {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save as");
            File selected = chooser.showSaveDialog(null);
            if (selected != null) {
                file = Path.of(selected.getAbsolutePath());
                saveFile();
            }
        }
    }
}
