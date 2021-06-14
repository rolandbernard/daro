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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CreatePlaygroundPage extends Page {
    private TextInput nameField;
    private CustomButton saveButton;
    private VBox form;

    public CreatePlaygroundPage() {
        Heading heading = new Heading("Create a new playground", "Give it a name and get started!");
        nameField = new TextInput("Playground name", null);
        saveButton = new CustomButton("\ue161", "Create the playground", true);
        saveButton.setOnMouseClicked(this::createPlayground);
        form = new VBox(nameField, saveButton);
        form.setAlignment(Pos.TOP_RIGHT);
        form.setSpacing(20);

        this.getChildren().addAll(heading, form);
    }

    private void createPlayground(MouseEvent mouseEvent) {
        Callout callout = new Callout("", ThemeColor.RED.toString());
        if (validateNameField()) {
            String error = PlaygroundHandler.createPlayground(nameField.getValue());
            if (error == null) {
                try {
                    View.updateView(this, new EditorView(PlaygroundHandler.getPlaygroundFile(nameField.getValue() + ".daro")));
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

    private boolean validateNameField() {
        return nameField.getValue().matches("^[a-zA-Z0-9_]+$");
    }
}
