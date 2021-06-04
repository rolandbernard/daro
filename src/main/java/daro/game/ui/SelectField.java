package daro.game.ui;

import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;

import java.util.List;


public class SelectField extends ChoiceBox<String> {
    public SelectField(List<String> choices) {
        super(FXCollections.observableArrayList(choices));
        this.getStyleClass().add("input-field");
        this.setValue(choices.get(0));
    }
}
