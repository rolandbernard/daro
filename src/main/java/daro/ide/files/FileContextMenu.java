package daro.ide.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 * This is the context menu that will be displayed for right clicks on the file
 * browser.
 * 
 * @author Roland Bernard
 */
public class FileContextMenu extends ContextMenu {

    /**
     * Set the current context of the menu.
     *
     * @param file       The file the menu refers to
     * @param parent     The parent of file operations (e.g. new file, new folder)
     * @param toReload   The node that must be reloaded after execution
     * @param onFileOpen The function to call for opening files
     */
    public void setContext(Path file, Path parent, FileTreeItem toReload, Consumer<Path> onFileOpen) {
        getItems().clear();
        MenuItem newFile = new MenuItem("New file");
        newFile.setOnAction(event -> {
            FileNameDialog dialog = new FileNameDialog("");
            dialog.showAndWait().ifPresent(name -> {
                try {
                    Files.createFile(Path.of(parent.toString(), name));
                    toReload.reload(false);
                } catch (IOException e) {}
            });
        });
        MenuItem newFolder = new MenuItem("New folder");
        newFolder.setOnAction(event -> {
            FileNameDialog dialog = new FileNameDialog("");
            dialog.showAndWait().ifPresent(name -> {
                try {
                    Files.createDirectory(Path.of(parent.toString(), name));
                    toReload.reload(false);
                } catch (IOException e) {}
            });
        });
        getItems().addAll(newFile, newFolder);
        if (file != null) {
            MenuItem delete = new MenuItem("Delete");
            delete.setOnAction(event -> {
                try {
                    Files.delete(file);
                    toReload.reload(false);
                } catch (IOException e) {}
            });
            MenuItem rename = new MenuItem("Rename");
            rename.setOnAction(event -> {
                FileNameDialog dialog = new FileNameDialog(file.getFileName().toString());
                dialog.showAndWait().ifPresent(name -> {
                    try {
                        Files.move(file, Path.of(parent.toString(), name));
                        toReload.reload(false);
                    } catch (IOException e) {}
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
