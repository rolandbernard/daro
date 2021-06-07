package daro.game.ui;

import daro.game.pages.Page;
import javafx.collections.FXCollections;
import javafx.scene.Cursor;
import javafx.scene.control.ChoiceBox;

import java.util.List;
import java.util.Map;


public class SelectField<T> extends InputField {
    private ChoiceBox<String> selectField;
    private Map<T, String> choices;

    public SelectField(Map<T, String> choices, T value) {
        init(choices, value);
    }

    public SelectField(Map<T, String> choices, T value, String label) {
        super(label);
        init(choices, value);
    }

    private void init(Map<T, String> choices, T value) {
        selectField = new ChoiceBox<>(FXCollections.observableArrayList(choices.values()));
        this.choices = choices;
        selectField.getStyleClass().add("input-field");
        if(choices != null && choices.size() > 0) {
            selectField.setValue(value == null ? choices.get(choices.keySet().toArray()[0]) : choices.get(value));
        }
        selectField.setPrefWidth(Page.INNER_WIDTH);
        this.getChildren().add(selectField);
        this.setCursor(Cursor.HAND);
    }

    public T getValue() {
        for(T key : choices.keySet()) {
            if(choices.get(key).equals(selectField.getValue()))
                return key;
        }
        return null;
    }
}
