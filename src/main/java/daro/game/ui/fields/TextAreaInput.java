package daro.game.ui.fields;

import javafx.scene.control.TextArea;

/**
 * <strong>UI: <em>Field</em></strong><br>
 * A custom TextArea input field
 *
 * @author Daniel Planötscher
 */
public class TextAreaInput extends InputField {
    private TextArea field;

    /**
     * Generates a default textarea input
     *
     * @param label the label of the input
     * @param help  its help text
     */
    public TextAreaInput(String label, String help) {
        super(label, help);
        field = new TextArea();
        field.getStyleClass().add("input-field");
        field.setWrapText(true);
        field.setMinHeight(300);
        getChildren().add(field);
    }

    @Override
    public String getValue() {
        return this.field.getText();
    }
}
