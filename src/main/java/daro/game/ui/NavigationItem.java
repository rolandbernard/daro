package daro.game.ui;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * <strong>UI: <em>Component</em></strong><br>
 * A navigation item for the main-navigation
 *
 * @author Daniel Planötscher
 */
public class NavigationItem extends HBox {

    /**
     * Generates a basic Navigation item
     *
     * @param icon      a string containing the unicode of a Material-Icon (e.g.
     *                  \ue021)
     * @param label     a string containing the label next to the icon
     * @param isDefault a boolean if the Navigation Item has the active class as a
     *                  default
     */
    public NavigationItem(String icon, String label, boolean isDefault) {
        Text labelText = new Text(label);
        labelText.getStyleClass().add("text");
        labelText.setStyle("-fx-font-size: 20px;");

        Icon iconText = new Icon(icon);
        iconText.setStyle("-fx-font-size: 20px;");

        setStyle("-fx-font-weight: bold; -fx-background-radius: 10px; -fx-padding: 12px 16px;");
        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(20);
        this.setPrefWidth(240);
        this.getChildren().addAll(iconText, labelText);
        this.getStyleClass().add("nav-item");
        Interaction.setClickable(this, false);
        if (isDefault) {
            this.getStyleClass().add("active");
        }
    }
}
