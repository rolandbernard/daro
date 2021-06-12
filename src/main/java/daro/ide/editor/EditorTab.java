package daro.ide.editor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import daro.lang.ast.Position;
import daro.lang.interpreter.DaroException;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.stage.FileChooser;

/**
 * This class implements a {@link Tab} containing a {@link TextEditor}. The tab
 * can also be linked to a concrete file.
 * 
 * @author Roland Bernard
 */
public class EditorTab extends Tab {
    private final EditorTabs parent;
    private Path file;
    private TextEditor editor;
    private boolean unsaved;

    /**
     * Create a new {@link EditorTab} that is not linked to a file.
     *
     * @param content The starting content of the tab
     * @param parent  The parent tabs handle
     */
    public EditorTab(String content, EditorTabs parent) {
        this.parent = parent;
        this.file = null;
        this.editor = new TextEditor(content);
        init();
    }

    /**
     * Create a new {@link EditorTab} that is linked to the given file. The starting
     * content of the tab will be loaded from the file directly.
     *
     * @param file   The file for this tab
     * @param parent The parent tabs handle
     */
    public EditorTab(Path file, EditorTabs parent) {
        this.parent = parent;
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

    /**
     * Update the title of this tab
     */
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

    /**
     * Initialize the tab after having set the file and editor.
     */
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

    /**
     * Get the file the tab is linked to.
     *
     * @return The tabs file
     */
    public Path getFile() {
        return file;
    }

    /**
     * Get whether the tab contains unsaved data.
     *
     * @return true if the text has not been saved since the last change, false
     *         otherwise
     */
    public boolean isUnsaved() {
        return unsaved;
    }

    /**
     * Get the text currently displayed in the tabs {@link TextEditor}.
     *
     * @return The tabs text
     */
    public String getEditorContent() {
        return editor.getText();
    }

    /**
     * Get all the breakpoints set in the linked {@link TextEditor}.
     *
     * @return The tabs breakpoints
     */
    public Set<Integer> getBreakpoints() {
        return editor.getBreakpoints();
    }

    /**
     * Set the context menu that should be used for the {@link TextEditor}. This
     * will also set the menus current editor to this one.
     *
     * @param menu The menu to set
     */
    public void setEditorContextMenu(EditorContextMenu menu) {
        menu.setEditor(editor);
        editor.setContextMenu(menu);
    }

    /**
     * Reset the highlighting of the tabs editor. This will also clear the currently
     * displayed debug position and error.
     */
    public void resetHighlighting() {
        editor.resetHighlighting();
    }

    /**
     * Highlight the given position as the debuggers current position. This will not
     * consider the positions file.
     *
     * @param position The position to mark
     */
    public void highlightDebug(Position position) {
        editor.highlightDebug(position);
    }

    /**
     * Highlight the given position as the location of an error.
     *
     * @param error The error to mark
     */
    public void highlightError(DaroException error) {
        editor.highlightError(error);
    }

    /**
     * Save the content of the tab inside of a file that should be requested from
     * the user using a {@link FileChooser}.
     */
    public void saveFileAs() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save as");
        File selected = chooser.showSaveDialog(null);
        if (selected != null) {
            file = Path.of(selected.getAbsolutePath());
            saveFile();
            parent.reopen(this);
        }
    }

    /**
     * Save the content of the tab inside of the linked file. If no file is linked
     * to the tab, ask the user to select a file using a {@link FileChooser}.
     */
    public void saveFile() {
        if (file != null) {
            try {
                Files.writeString(file, editor.getText());
                unsaved = false;
                updateTitle();
            } catch (IOException e) {}
        } else {
            saveFileAs();
        }
    }
}
