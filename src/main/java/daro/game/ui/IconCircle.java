package daro.game.ui;

import daro.game.main.ThemeColor;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class IconCircle extends StackPane {
    private static final double DIMENSION = 50;

    public IconCircle(String color, String icon, boolean clickable, boolean translated) {
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
        if (translated) {
            setTranslateX(DIMENSION / 2);
            setTranslateY(-DIMENSION / 2);
        }
    }

    public static IconCircle getDeleteButton(boolean translated) {
        return new IconCircle(ThemeColor.RED.toString(), "\ue872", true, translated);
    }

    public static IconCircle getCheckIcon(boolean translated) {
        IconCircle check = new IconCircle(ThemeColor.GREEN.toString(), "\ue876", false, false);
        if (translated) {
            check.setTranslateX(-DIMENSION / 2);
        }
        return check;
    }
}
