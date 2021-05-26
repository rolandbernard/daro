package daro.ide.main;

import daro.ide.files.FilePane;
import javafx.scene.control.SplitPane;

public class Explorer extends SplitPane {

    public Explorer() {
        FilePane files = new FilePane();
        Workspace workspace = new Workspace();
        files.setOnFileOpen(file -> {
            workspace.openNewFile(file);
        });
        getItems().addAll(files, workspace);
        setDividerPositions(0.2);
    }
}
