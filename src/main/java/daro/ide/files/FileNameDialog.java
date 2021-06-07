package daro.ide.files;

import javafx.scene.control.TextInputDialog;

/**
 * This is a simple input window that asks the user to enter a filename.
 * 
 * @author Roland Bernard
 */
public class FileNameDialog extends TextInputDialog {

    /**
     * Create a new {@link FileNameDialog} for the given initial name.
     *
     * @param initial The initial filename
     */
    public FileNameDialog(String initial) {
        super(initial);
        setGraphic(null);
        setHeaderText(null);
        setResizable(true);
        setWidth(300);
        setHeight(125);
        setTitle("Enter new name");

        getDialogPane().getStylesheets()
            .add(FileNameDialog.class.getResource("/daro/ide/styles/dialog.css").toExternalForm());
    }
}
