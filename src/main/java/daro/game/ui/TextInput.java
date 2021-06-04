package daro.game.ui;

import javafx.scene.control.TextField;

public class TextInput extends TextField {
    public TextInput() {
        render();
    }

    public TextInput(String label) {
        render();
    }

    private void render() {
        this.setStyle("");
        this.getStyleClass().add("input-field");
    }
}
