package daro.ide.editor;

import javafx.scene.control.Alert;

public class ConfirmDialog extends Alert {

    public ConfirmDialog(String message) {
        super(AlertType.CONFIRMATION);
        setGraphic(null);
        setHeaderText(null);
        setResizable(true);
        setWidth(200);
        setHeight(125);
        setTitle(message);
        setContentText(message);

        getDialogPane().getStylesheets()
            .add(ConfirmDialog.class.getResource("/daro/ide/styles/dialog.css").toExternalForm());
    }
}
