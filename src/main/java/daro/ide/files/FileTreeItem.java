package daro.ide.files;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class FileTreeItem extends TreeItem<String> {
    private File file;
    private boolean loaded;

    public FileTreeItem(File file) {
        super(file.getName());
        this.file = file;
        this.loaded = false;
    }

    @Override
    public ObservableList<TreeItem<String>> getChildren() {
        if (!loaded) {
            List<TreeItem<String>> children;
            children = Arrays.stream(file.listFiles())
                .map(child -> new FileTreeItem(child))
                .collect(Collectors.toList());
            super.getChildren().addAll(children);
            loaded = true ;
        }
        return super.getChildren();
    }

    @Override
    public boolean isLeaf() {
        return !file.isDirectory();
    }
}
