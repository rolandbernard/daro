package daro.game.ui;

import daro.game.main.ThemeColor;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class DeleteButton extends StackPane {
    public DeleteButton() {
        Text icon = new Text("\ue872");
        icon.getStyleClass().add("icon");
        setMaxWidth(50);
        setMaxHeight(50);
        Interaction.setClickable(this, false);
        setLayoutY(-25);
        setLayoutX(-25);
        setAlignment(Pos.CENTER);
        getChildren().add(icon);
        setStyle("-fx-background-radius: 25px; -fx-background-color: " + ThemeColor.RED);
    }
}
