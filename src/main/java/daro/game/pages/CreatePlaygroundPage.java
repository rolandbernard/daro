package daro.game.pages;

import daro.game.ui.Heading;
import javafx.scene.control.TextField;

public class CreatePlaygroundPage extends Page {
    private TextField nameField;

    public CreatePlaygroundPage() {
        Heading heading = new Heading("Create a new playground", "Give it a name and get started!");
        nameField = new TextField();
        nameField.setStyle("-fx-font-size: 24px; -fx-background: rgba(255, 255, 255, 0.5)");

        this.getChildren().addAll(heading, nameField);
    }

    private boolean validateNameField() {
        return nameField.getText().matches("[^\\s]+");
    }
}
