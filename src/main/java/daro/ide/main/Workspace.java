package daro.ide.main;

import java.nio.file.Path;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;

public class Workspace extends SplitPane {

    public Workspace() {
        setOrientation(Orientation.VERTICAL);
        getItems().addAll();
        setDividerPositions(0.7);
    }

    public void openNewFile(Path file) {
        
    }
}
