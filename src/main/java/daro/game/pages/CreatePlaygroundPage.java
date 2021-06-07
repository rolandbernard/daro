package daro.game.pages;

import daro.game.io.UserData;
import daro.game.ui.Callout;
import daro.game.ui.CustomButton;
import daro.game.ui.Heading;
import daro.game.ui.TextInput;
import daro.game.views.EditorView;
import daro.game.views.MenuView;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CreatePlaygroundPage extends Page {
    private TextInput nameField;
    private CustomButton saveButton;
    private Callout callout;
    private VBox form;

    public CreatePlaygroundPage() {
        Heading heading = new Heading("Create a new playground", "Give it a name and get started!");
        nameField = new TextInput("Playground name");
        saveButton = new CustomButton("\ue161", "Create the playground", 60, true);
        saveButton.setOnMouseClicked(this::createPlayground);
        callout = new Callout("", "#fc323f");
        form = new VBox(nameField, saveButton);
        form.setAlignment(Pos.TOP_RIGHT);
        form.setSpacing(20);

        this.getChildren().addAll(heading, form);
    }

    private void createPlayground(MouseEvent mouseEvent) {
        if(validateNameField()) {
            String error = UserData.createPlayground(nameField.getValue());
            if(error == null) {
                try {
                    this.getScene().setRoot(new EditorView(UserData.getPlaygroundFile(nameField.getValue() + ".daro")));
                } catch (IOException e) {
                    this.getScene().setRoot(new MenuView(new PlaygroundPage()));
                }
            } else {
                form.getChildren().add(0, callout);
                callout.setText(error);
            }
        } else {
            form.getChildren().add(0, callout);
            callout.setText("A playground's name can only contain letters, numbers and underlines (_).");
        }
    }

    private boolean validateNameField() {
        return nameField.getValue().matches("^[a-zA-Z0-9_]+$");
    }
}
