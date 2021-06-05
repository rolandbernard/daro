package daro.ide.editor;

import javafx.scene.control.Alert;

/**
 * This is a simple confirmation window with the IDEs styles applied.
 * 
 * @author Roland Bernard
 */
public class ConfirmDialog extends Alert {

    /**
     * Create a new {@link ConfirmDialog} asking the given message.
     *
     * @param message The message that should be displayed
     */
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
