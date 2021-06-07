package daro.game.ui;

import javafx.scene.control.TextField;

public class TextInput extends InputField {

    private TextField field;

    public TextInput() {
        render();
    }

    public TextInput(String label) {
        super(label);
        render();
    }

    private void render() {
        field = new TextField();
        field.getStyleClass().add("input-field");
        this.getChildren().add(field);
    }

    public String getValue() {
        return field.getText();
    }
}
