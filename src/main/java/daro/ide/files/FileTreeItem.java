package daro.ide.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
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

    public void reload() {
        Map<String, TreeItem<String>> children = super.getChildren().stream()
            .collect(Collectors.toMap(item -> item.getValue(), item -> item));
        try {
            Map<String, Path> files = Files.list(file)
                .collect(Collectors.toMap(file -> file.getFileName().toString(), file -> file));
            super.getChildren().removeIf(item -> !files.containsKey(item.getValue()));
            super.getChildren().addAll(
                files.entrySet().stream()
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
        } catch (IOException e) { }
    }

    @Override
    public ObservableList<TreeItem<String>> getChildren() {
        if (!loaded) {
            loaded = true;
            List<TreeItem<String>> children;
            try {
                children = Files.list(file)
                    .sorted((a, b) -> {
                        int directory = Boolean.compare(Files.isDirectory(b), Files.isDirectory(a));
                        if (directory != 0) {
                            return directory;
                        } else {
                            return a.getFileName().toString().compareTo(b.getFileName().toString());
                        }
                    })
                    .map(child -> new FileTreeItem(child))
                    .collect(Collectors.toList());
            } catch (IOException e) {
                children = List.of();
            }
            super.getChildren().addAll(children);
        }
        return super.getChildren();
    }

    @Override
    public boolean isLeaf() {
        return !Files.isDirectory(file);
    }
}
