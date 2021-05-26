package daro.ide.files;

import java.nio.file.Path;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class FileContextMenu extends ContextMenu {

    public void setContext(Path file) {
        getItems().clear();
        getItems().addAll(new MenuItem("Test"));
    }
}
