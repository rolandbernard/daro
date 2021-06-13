package daro.game.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    /**
     * Returns the current value of the field
     *
     * @return a string containing the value
     */
    @Override
    public String getValue() {
        return field.getText();
    }

    @Override
    public void onChange(EventHandler<ActionEvent> handler) {
        field.setOnAction(handler);
    }
}
