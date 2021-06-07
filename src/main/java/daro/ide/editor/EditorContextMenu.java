package daro.ide.editor;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * This is the context menu that will be displayed for right clicks somewhere in
 * the editor.
 * 
 * @author Roland Bernard
 */
public class EditorContextMenu extends ContextMenu {
    private final EditorTabs tabs;

    /**
     * Create a new {@link EditorContextMenu} linked to the given
     * {@link EditorTabs}.
     *
     * @param tabs The editor the context menu is for
     */
    public EditorContextMenu(EditorTabs tabs) {
        this.tabs = tabs;
        setEditor(null);
    }

    /**
     * Set the {@link TextEditor} the context menu is for.
     *
     * @param editor The editor the context menu is currently for
     */
    public void setEditor(TextEditor editor) {
        getItems().clear();
        MenuItem save = new MenuItem("Save");
        save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        save.setOnAction(event -> {
            tabs.saveOpen();
        });
        MenuItem saveAs = new MenuItem("Save as");
        saveAs.setOnAction(event -> {
            tabs.saveOpenAs();
        });
        getItems().addAll(save, saveAs);
        if (editor != null) {

        }
    }
}
