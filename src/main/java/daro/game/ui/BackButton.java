package daro.game.ui;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;


/**
 * <strong>UI: <em>Component</em></strong><br>
 * A simple component that can be used as BackButton
 *
 * @author Daniel Planötscher
 */
public class BackButton extends HBox {

    /**
     * Generates a new BackButton
     *
     * @param label the label next to the arrow to the left
     */
    public BackButton(String label) {
        Icon iconText = new Icon("\ue5c4");
        iconText.getStyleClass().add("icon");

        Text labelText = new Text(label);
        labelText.getStyleClass().add("text");

        getChildren().addAll(iconText, labelText);
        Interaction.setClickable(this, false);
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(15);
    }
}
