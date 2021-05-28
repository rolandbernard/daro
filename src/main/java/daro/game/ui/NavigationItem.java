package daro.game.ui;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class NavigationItem extends HBox {

    /**
     * <strong>UI: <em>Component</em></strong><br>
     * A navigation item for the main-navigation
     * 
     * @param icon
     *            a string containing the unicode of a Material-Icon (e.g. \ue021)
     * @param label
     *            a string containing the label next to the icon
     * @param isDefault
     *            a boolean if the Navigation Item has the active class as a default
     */
    public NavigationItem(String icon, String label, boolean isDefault) {
        Text labelText = new Text(label);
        labelText.getStyleClass().add("text");
        labelText.setStyle("-fx-font-size: 20px;");

        Text iconText = new Text(icon);
        iconText.getStyleClass().add("icon");
        iconText.setStyle("-fx-font-size: 20px;");

        this.setStyle("-fx-font-weight: bold; -fx-background-radius: 10px; -fx-padding: 12px 16px;");
        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(20);
        this.setPrefWidth(240);
        this.setCursor(Cursor.HAND);
        this.getChildren().addAll(iconText, labelText);
        this.getStyleClass().add("nav-item");
        if (isDefault) {
            this.getStyleClass().add("active");
        }
    }
}
