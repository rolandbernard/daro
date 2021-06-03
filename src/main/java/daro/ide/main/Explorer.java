package daro.ide.main;

import daro.ide.files.FilePane;
import javafx.scene.control.SplitPane;

public class Explorer extends SplitPane {
    private FilePane files;
    private Workspace workspace;

    public Explorer() {
        files = new FilePane();
        files.setPrefWidth(200);
        files.setMaxWidth(450);
        workspace = new Workspace();
        files.setOnFileOpen(file -> {
            workspace.openNewFile(file);
        });
        getItems().addAll(files, workspace);
    }

    public boolean hasUnsavedFile() {
        return workspace.hasUnsavedFile();
    }
}
