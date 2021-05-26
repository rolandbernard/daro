package daro.ide.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FileTreeItem extends TreeItem<String> {
    private final Path file;
    private boolean loaded;

    public FileTreeItem(Path file) {
        super(file.getFileName().toString());
        this.file = file;
        this.loaded = false;
        Image image;
        if (Files.isDirectory(file)) {
            image = new Image(FileTreeItem.class.getResourceAsStream("folder.png"));
        } else {
            image = new Image(FileTreeItem.class.getResourceAsStream("file.png"));
        }
        ImageView icon = new ImageView(image);
        icon.setFitWidth(20);
        icon.setFitHeight(20);
        setGraphic(icon);
    }

    public Path getFile() {
        return file;
    }

    @Override
    public ObservableList<TreeItem<String>> getChildren() {
        if (!loaded) {
            List<TreeItem<String>> children;
            try {
                children = Files.list(file)
                    .map(child -> new FileTreeItem(child))
                    .collect(Collectors.toList());
            } catch (IOException e) {
                children = List.of();
            }
            super.getChildren().addAll(children);
            loaded = true ;
        }
        return super.getChildren();
    }

    @Override
    public boolean isLeaf() {
        return !Files.isDirectory(file);
    }
}
