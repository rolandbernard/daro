package daro.ide.files;

import java.io.File;
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

    public void setRoot(Path file) {
        if (file != null) {
            FileTree view = new FileTree(file);
            view.setOnFileOpen(onFileOpen);
            setCenter(view);
        } else {
            Button open = new Button("Open folder");
            open.setOnAction(event -> {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Select a folder");
                File newFile = chooser.showDialog(null);
                if (newFile != null) {
                    setRoot(Path.of(newFile.getAbsolutePath()));
                }
            });
            setCenter(open);
        }
    }

    public void setOnFileOpen(Consumer<Path> onFileOpen) {
        this.onFileOpen = onFileOpen;
    }
}
