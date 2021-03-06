package daro.game.ui.fields;

import daro.game.ui.Icon;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * <strong>UI: <em>Template</em></strong><br>
 * An abstract class defining the key features of an InputField
 *
 * @author Daniel Planötscher
 */
public abstract class InputField extends VBox {

    /**
     * @param label the label for the Input Field (null if not needed)
     * @param help  a simple help text (null if not needed)
     */
    public InputField(String label, String help) {
        setSpacing(10);
        if (label != null && label.length() > 0) {
            Text text = new Text(label);
            text.getStyleClass().addAll("text", "bold");
            getChildren().add(text);
        }

        if (help != null && help.length() > 0) {
            Text helpText = new Text(help);
            helpText.getStyleClass().add("text");
            helpText.setStyle("-fx-font-size: 12px");
            Icon helpIcon = new Icon("\ue887");
            helpIcon.setStyle("-fx-font-size: 14px");
            HBox helpBox = new HBox(helpIcon, helpText);
            helpBox.setSpacing(5);
            helpBox.setAlignment(Pos.CENTER_LEFT);
            getChildren().add(helpBox);
        }
    }

    /**
     * Returns the current value of the Input Field
     *
     * @return a string containing the value
     */
    public abstract Object getValue();
}
