package daro.ide.files;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;

public class FilePane extends BorderPane {
    private Consumer<Path> onFileOpen = null;

    public FilePane() {
        setRoot(null);
    }

    public void setRoot(Path root) {
        if (root != null) {
            FileTree view = new FileTree(root);
            view.setOnFileOpen(file -> {
                if (Files.isDirectory(file)) {
                    setRoot(file);
                } else {
                    onFileOpen.accept(file);
                }
            });
            setCenter(view);
        } else {
            Button open = new Button("Open folder");
            open.setOnAction(event -> {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Select a folder");
                File file = chooser.showDialog(null);
                if (file != null) {
                    setRoot(Path.of(file.getAbsolutePath()));
                }
            });
            setCenter(open);
        }
    }

    public void setOnFileOpen(Consumer<Path> onFileOpen) {
        this.onFileOpen = onFileOpen;
    }
}
