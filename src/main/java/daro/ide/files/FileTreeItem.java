package daro.ide.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.text.Text;

public class FileTreeItem extends TreeItem<String> {
    private final Path file;
    private boolean loaded;

    public FileTreeItem(Path file) {
        super(file.getFileName().toString());
        this.file = file;
        this.loaded = false;
        Text icon = new Text();
        icon.getStyleClass().add("icon");
        if (Files.isDirectory(file)) {
            icon.setText("\ue2c7");
        } else {
            icon.setText("\ue873");
        }
        setGraphic(icon);
    }

    public Path getFile() {
        return file;
    }

    public void reload(boolean recursive) {
        if (loaded && !isLeaf()) {
            if (isExpanded()) {
            } else {
                loaded = false;
                getChildren();
                if (recursive) {
                    super.getChildren().forEach(item -> ((FileTreeItem)item).reload(true));
                }
            }
        }
    }

    @Override
    public ObservableList<TreeItem<String>> getChildren() {
        if (!loaded && !isLeaf()) {
            loaded = true;
            Map<String, TreeItem<String>> children =
                super.getChildren().stream().collect(Collectors.toMap(item -> item.getValue(), item -> item));
            try {
                Map<String, Path> files =
                    Files.list(file).collect(Collectors.toMap(file -> file.getFileName().toString(), file -> file));
                super.getChildren().removeIf(item -> !files.containsKey(item.getValue()));
                super.getChildren().addAll(
                        files.entrySet()
                        .stream()
                        .filter(entry -> !children.containsKey(entry.getKey()))
                        .map(entry -> new FileTreeItem(entry.getValue()))
                        .collect(Collectors.toList())
                        );
                super.getChildren().sort((itemA, itemB) -> {
                    Path a = ((FileTreeItem)itemA).getFile();
                    Path b = ((FileTreeItem)itemB).getFile();
                    int directory = Boolean.compare(Files.isDirectory(b), Files.isDirectory(a));
                    if (directory != 0) {
                        return directory;
                    } else {
                        return a.getFileName().toString().compareTo(b.getFileName().toString());
                    }
                });
            } catch (IOException e) {}
        }
        return super.getChildren();
    }

    @Override
    public boolean isLeaf() {
        return !Files.isDirectory(file);
    }
}
