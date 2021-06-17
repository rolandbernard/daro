package daro.game.ui;

import daro.game.main.ThemeColor;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class CreateButton extends HBox {

    /**
     * Creates a new Button, usually used for the creation of new elements.
     *
     * @param label a label describing what clicking the button does.
     */
    public CreateButton(String label) {
        Text plus = new Text("\ue145");
        plus.getStyleClass().add("icon");
        Text labelText = new Text(label);
        labelText.getStyleClass().addAll("text", "bold");
        this.setSpacing(10);
        this.setMinHeight(80);
        this.setStyle("-fx-background-radius: 15px; -fx-background-color: " + ThemeColor.LIGHT_BACKGROUND);
        Interaction.setClickable(this, true);
        this.setCursor(Cursor.HAND);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(plus, labelText);
    }
}
