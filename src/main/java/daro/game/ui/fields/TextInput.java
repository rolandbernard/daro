package daro.game.ui.fields;

import javafx.scene.control.TextField;

/**
 * <strong>UI: <em>Field</em></strong><br>
 * A custom one-line text Input Field
 *
 * @author Daniel Planötscher
 */
public class TextInput extends InputField {
    private TextField field;

    /**
     * Generates a default text input
     *
     * @param label the label of the input
     * @param help  its help text
     */
    public TextInput(String label, String help) {
        super(label, help);
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
}
