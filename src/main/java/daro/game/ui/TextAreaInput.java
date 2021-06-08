package daro.game.ui;

import javafx.scene.control.TextArea;

public class TextAreaInput extends InputField {

    private TextArea field;

    public TextAreaInput() {
        init();
    }

    public TextAreaInput(String label) {
        super(label);
        init();
    }

    private void init() {
        field = new TextArea();
        field.getStyleClass().add("input-field");
        field.setWrapText(true);
        this.getChildren().add(field);
    }

    @Override
    public String getValue() {
        return this.field.getText();
    }
}
