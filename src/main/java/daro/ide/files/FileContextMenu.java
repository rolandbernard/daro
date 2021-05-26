package daro.ide.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class FileContextMenu extends ContextMenu {

    public void setContext(Path file, Path parent, FileTreeItem toReload, Consumer<Path> onFileOpen) {
        getItems().clear();
        MenuItem newFile = new MenuItem("New file");
        newFile.setOnAction(event -> {
            FileNameDialog dialog = new FileNameDialog("");
            dialog.showAndWait().ifPresent(name -> {
                try {
                    Files.createFile(Path.of(parent.toString(), name));
                    toReload.reload();
                } catch (IOException e) { }
            });
        });
        MenuItem newFolder = new MenuItem("New folder");
        newFolder.setOnAction(event -> {
            FileNameDialog dialog = new FileNameDialog("");
            dialog.showAndWait().ifPresent(name -> {
                try {
                    Files.createDirectory(Path.of(parent.toString(), name));
                    toReload.reload();
                } catch (IOException e) { }
            });
        });
        getItems().addAll(newFile, newFolder);
        if (file != null) {
            MenuItem delete = new MenuItem("Delete");
            delete.setOnAction(event -> {
                try {
                    Files.delete(file);
                    toReload.reload();
                } catch (IOException e) { }
            });
            MenuItem rename = new MenuItem("Rename");
            rename.setOnAction(event -> {
                FileNameDialog dialog = new FileNameDialog(file.getFileName().toString());
                dialog.showAndWait().ifPresent(name -> {
                    try {
                        Files.move(file, Path.of(parent.toString(), name));
                        toReload.reload();
                    } catch (IOException e) { }
                });
            });
            getItems().addAll(new SeparatorMenuItem(), rename, delete, new SeparatorMenuItem());
            if (Files.isDirectory(file)) {
                MenuItem open = new MenuItem("Open folder");
                open.setOnAction(event -> {
                    onFileOpen.accept(file);
                });
                getItems().add(open);
            } else {
                MenuItem open = new MenuItem("Open file");
                open.setOnAction(event -> {
                    onFileOpen.accept(file);
                });
                getItems().add(open);
            }
        }
    }
}
