package daro.game.ui;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TextInput extends VBox {

    private TextField field;

    public TextInput() {
        render();
    }

    public TextInput(String label) {
        Text text = new Text(label);
        text.getStyleClass().addAll("text", "bold");
        this.getChildren().add(text);
        this.setSpacing(10);
        render();
    }

    private void render() {
        field = new TextField();
        field.getStyleClass().add("input-field");
        this.getChildren().add(field);
    }

    public String getText() {
        return field.getText();
    }
}
