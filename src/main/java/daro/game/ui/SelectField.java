package daro.game.ui;

import daro.game.pages.Page;
import javafx.collections.FXCollections;
import javafx.scene.Cursor;
import javafx.scene.control.ChoiceBox;

import java.util.List;


public class SelectField extends InputField {
    private ChoiceBox<String> selectField;

    public SelectField(List<String> choices, String value) {
        init(choices, value);
    }

    public SelectField(List<String> choices, String value, String label) {
        super(label);
        init(choices, value);
    }

    private void init(List<String> choices, String value) {
        selectField = new ChoiceBox<>(FXCollections.observableArrayList(choices));
        selectField.getStyleClass().add("input-field");
        selectField.setValue(value == null ? choices.get(0) : value);
        selectField.setPrefWidth(Page.INNER_WIDTH);
        this.getChildren().add(selectField);
        this.setCursor(Cursor.HAND);
    }

    public String getValue() {
        return selectField.getValue();
    }
}
