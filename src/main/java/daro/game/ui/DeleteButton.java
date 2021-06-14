package daro.game.ui;

import daro.game.main.ThemeColor;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class DeleteButton extends StackPane {
    private static final double DIMENSION = 50;

    public DeleteButton() {
        Text icon = new Text("\ue872");
        icon.getStyleClass().add("icon");
        setMaxWidth(DIMENSION);
        setMaxHeight(DIMENSION);
        setMinWidth(DIMENSION);
        setMinHeight(DIMENSION);
        Interaction.setClickable(this, false);
        setAlignment(Pos.CENTER);
        getChildren().add(icon);
        setStyle("-fx-background-radius: " + DIMENSION / 2 + "px; -fx-background-color: " + ThemeColor.RED);
    }
}
