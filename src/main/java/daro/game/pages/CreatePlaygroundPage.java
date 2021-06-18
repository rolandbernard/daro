package daro.game.pages;

import daro.game.io.PlaygroundHandler;
import daro.game.main.ThemeColor;
import daro.game.ui.Callout;
import daro.game.ui.CustomButton;
import daro.game.ui.Heading;
import daro.game.ui.fields.TextInput;
import daro.game.views.EditorView;
import daro.game.views.MenuView;
import daro.game.views.View;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;

/**
 * <strong>UI: <em>Page</em></strong><br>
 * A page that shows a TextInput to create new playgrounds.
 */
public class CreatePlaygroundPage extends Page {
    private TextInput nameField;
    private CustomButton saveButton;
    private VBox form;

    /**
     * Generates a standard create playground page
     */
    public CreatePlaygroundPage() {
        Heading heading = new Heading("Create a new playground", "Give it a name and get started!");
        nameField = new TextInput("Playground name", null);
        saveButton = new CustomButton("\ue161", "Create the playground", true);
        saveButton.setOnMouseClicked(e -> createPlayground());
        form = new VBox(nameField, saveButton);
        form.setAlignment(Pos.TOP_RIGHT);
        form.setSpacing(20);

        getChildren().addAll(heading, form);
    }

    /**
     * Creates a new playground, if there is an error it shows the error in a callout.
     */
    private void createPlayground() {
        Callout callout = new Callout("", ThemeColor.RED.toString());
        if (validateNameField()) {
            String error = PlaygroundHandler.createPlayground(nameField.getValue());
            if (error == null) {
                try {
                    String filename = nameField.getValue() + ".daro";
                    File playgroundFile = PlaygroundHandler.getPlaygroundFile(filename);
                    View.updateView(this, new EditorView(playgroundFile));
                } catch (IOException e) {
                    View.updateView(this, new MenuView(new PlaygroundPage()));
                }
            } else {
                form.getChildren().add(0, callout);
                callout.setText(error);
            }
        } else {
            form.getChildren().add(0, callout);
            callout.setText("A playground's name can only contain letters, numbers and underlines (_).");
        }
        callout.setOnClose(e -> form.getChildren().remove(callout));
    }

    /**
     * Validates the playground name using regex
     *
     * @return true if the name is valid
     */
    private boolean validateNameField() {
        return nameField.getValue().matches("^[a-zA-Z0-9_]+$");
    }
}
