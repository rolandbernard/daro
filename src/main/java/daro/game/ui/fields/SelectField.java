package daro.game.ui.fields;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.ChoiceBox;

import java.util.Map;

/**
 * <strong>UI: <em>Field</em></strong><br>
 * An generic class, describing a SelectField. The generic type is used as value
 * of the field
 *
 * @author Daniel Planötscher
 */
public class SelectField<T> extends InputField {
    private ChoiceBox<String> selectField;
    private Map<T, String> choices;

    /**
     * @param choices a map that maps values to its labels
     * @param value   the default value
     * @param label   the label for the field
     * @param help    the help text for the field
     */
    public SelectField(Map<T, String> choices, T value, String label, String help) {
        super(label, help);
        selectField = new ChoiceBox<>(FXCollections.observableArrayList(choices.values()));
        this.choices = choices;
        selectField.getStyleClass().add("input-field");
        if (choices.size() > 0) {
            selectField.setValue(value == null ? choices.get(choices.keySet().toArray()[0]) : choices.get(value));
        }
        this.getChildren().add(selectField);
        selectField.setPrefWidth(Integer.MAX_VALUE);
        this.setCursor(Cursor.HAND);
    }

    public T getValue() {
        return choices.entrySet()
                .stream().filter(c -> c.getValue().equals(selectField.getValue()))
                .map(Map.Entry::getKey)
                .findFirst().orElse(null);
    }

    /**
     * Event that describes a change on the select field
     *
     * @param handler the handler that defines the routine what should happen
     */
    public void onChange(EventHandler<ActionEvent> handler) {
        selectField.setOnAction(handler);
    }
}
