package daro.ide.files;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

public class FilePane extends BorderPane {
    private Consumer<Path> onFileOpen = null;

    public FilePane() {
        setRoot(null);
    }

    public void setRoot(Path root) {
        Button open = new Button("Open folder");
        open.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Select a folder");
            File file = chooser.showDialog(sceneProperty().get().getWindow());
            if (file != null) {
                setRoot(Path.of(file.getAbsolutePath()));
            }
        });
        if (root != null) {
            FileTree view = new FileTree(root);
            view.setOnFileOpen(file -> {
                if (Files.isDirectory(file)) {
                    setRoot(file);
                } else {
                    onFileOpen.accept(file);
                }
            });
            Text icon = new Text("\ue5d5");
            icon.getStyleClass().add("icon");
            Button refresh = new Button();
            refresh.setGraphic(icon);
            refresh.setOnAction(event -> {
                view.reload();
            });
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            HBox navigation = new HBox(open, spacer, refresh);
            navigation.setPadding(new Insets(2, 5, 2, 5));
            navigation.setAlignment(Pos.CENTER);
            setTop(navigation);
            setCenter(view);
        } else {
            setCenter(open);
        }
    }

    public void setOnFileOpen(Consumer<Path> onFileOpen) {
        this.onFileOpen = onFileOpen;
    }
}
