package daro.ide.main;

import java.nio.file.Path;

import daro.ide.files.FilePane;
import javafx.scene.control.SplitPane;

/**
 * This class implements a {@link SplitPane} containing a file browser in the
 * left side and a {@link Workspace} on the right side.
 * 
 * @author Roland Bernard
 */
public class Explorer extends SplitPane {
    private FilePane files;
    private Workspace workspace;

    /**
     * Create a new {@link Explorer}.
     */
    public Explorer() {
        files = new FilePane();
        files.setPrefWidth(200);
        files.setMaxWidth(450);
        workspace = new Workspace();
        files.setOnFileOpen(file -> {
            workspace.openFile(file);
        });
        getItems().addAll(files, workspace);
    }

    /**
     * Open the given path as the directory of the file browser.
     *
     * @param root The directory to open
     */
    public void openDirectory(Path root) {
        files.setRoot(root);
    }

    /**
     * This method is executed before closing the {@link Explorer} to make sure the
     * user is aware of potentially unsaved data.
     *
     * @return true if closing is allowed, false otherwise
     */
    public boolean allowClosing() {
        return workspace.allowClosing();
    }
}
