package daro.game.ui;

import daro.game.ui.fields.InputField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * <strong>UI: <em>Component</em></strong><br>
 * A simple component that handles groups of {@link InputField}s
 *
 * @author Daniel Planötscher
 */
public class FieldGroup extends VBox {
    private Text title;

    /**
     * Creates a standard FieldGroup
     *
     * @param name   the name of the FieldGroup displayed as heading
     * @param fields the fields it should containing
     */
    public FieldGroup(String name, InputField... fields) {
        title = new Text(name);
        title.getStyleClass().addAll("heading", "small", "text");
        this.setSpacing(20);
        this.getChildren().add(title);
        this.getChildren().addAll(fields);
    }

    /**
     * Updates the heading of the FieldGroup
     *
     * @param name the new name
     */
    public void setName(String name) {
        title.setText(name);
    }
}
