package daro.ide.files;

import javafx.scene.control.TextInputDialog;

public class FileNameDialog extends TextInputDialog {

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

