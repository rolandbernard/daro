package daro.game.ui.fields;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;

public class TextAreaInput extends InputField {
    private TextArea field;

    public TextAreaInput(String label, String help) {
        super(label, help);
        init();
    }

    private void init() {
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

    @Override
    public void onChange(EventHandler<ActionEvent> handler) {
        // TODO
    }
}
