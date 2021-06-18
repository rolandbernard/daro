package daro.game.ui;

import daro.game.main.ThemeColor;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;

/**
 * <strong>UI: <em>Component</em></strong><br>
 * A circle containing an icon
 *
 * @author Daniel Planötscher
 */
public class IconCircle extends StackPane {
    private static final double DIMENSION = 50;

    /**
     * Generates a custom icon circle
     *
     * @param color     the background color
     * @param icon      the icon string
     * @param clickable if the circle is clickable
     */
    public IconCircle(String color, String icon, boolean clickable) {
        Icon iconText = new Icon(icon);
        setMaxWidth(DIMENSION);
        setMaxHeight(DIMENSION);
        setMinWidth(DIMENSION);
        setMinHeight(DIMENSION);
        if (clickable) {
            Interaction.setClickable(this, false);
        }
        setAlignment(Pos.CENTER);
        getChildren().add(iconText);
        setStyle("-fx-background-radius: " + DIMENSION / 2 + "px; -fx-background-color: " + color);
    }

    /**
     * Generates a often used icon circle: delete button
     *
     * @param translated if should be translated (shifted towards top right)
     * @return the IconCircle instance
     */
    public static IconCircle getDeleteButton(boolean translated) {
        IconCircle deleteButton = new IconCircle(ThemeColor.RED.toString(), "\ue872", true);
        if (translated) {
            deleteButton.setTranslateX(DIMENSION / 2);
            deleteButton.setTranslateY(-DIMENSION / 2);
        }
        return deleteButton;
    }

    /**
     * Generates a often used icon circle: check icon
     *
     * @param translated if the circle should be shifted to the left
     * @return the IconCircle instance
     */
    public static IconCircle getCheckIcon(boolean translated) {
        IconCircle check = new IconCircle(ThemeColor.GREEN.toString(), "\ue876", false);
        if (translated) {
            check.setTranslateX(-DIMENSION / 2);
        }
        return check;
    }
}
