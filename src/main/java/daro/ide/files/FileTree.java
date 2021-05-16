package daro.ide.files;

import java.nio.file.Path;

import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;

public class FileTree extends Pane {

    public FileTree(Path file) {
        FileTreeItem root = new FileTreeItem(file.toFile());
        TreeView<String> view = new TreeView<>(root);
        getChildren().add(view);
    }
}


