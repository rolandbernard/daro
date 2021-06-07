package daro.game.ui;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public abstract class InputField extends VBox {

    /**
     * <strong>UI: <em>Template</em></strong><br>
     * An abstract class defining the key features of an InputField
     */
    public InputField() { }

    /**
     * <strong>UI: <em>Template</em></strong><br>
     * An abstract class defining the key features of an InputField
     *
     * @param label the label for the Input Field
     */
    public InputField(String label) {
        Text text = new Text(label);
        text.getStyleClass().addAll("text", "bold");
        this.getChildren().add(text);
        this.setSpacing(10);
    }

    /**
     * Returns the current value of the Input Field
     *
     * @return a string containing the value
     */
    public abstract Object getValue();
}
