package daro.ide.editor;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class EditorContextMenu extends ContextMenu {
    private final EditorTabs tabs;

    public EditorContextMenu(EditorTabs tabs) {
        this.tabs = tabs;
        setEditor(null);
    }

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
