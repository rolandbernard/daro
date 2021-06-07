package daro.game.ui;

import daro.game.main.Game;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class CustomButton extends HBox {
    /**
     * Creates a custom button with an icon
     *
     * @param icon   icon string e.g. \ue037 
     * @param text   text for the button
     * @param width  width of the button
     * @param height height of the button
     */
    public CustomButton(String icon, String text, double width, double height, boolean rounded) {
        this.setPrefWidth(width);
        this.setPrefHeight(height);
        this.setStyle("-fx-background-color: " + Game.colorTheme.get("accent"));
        init(icon, text, rounded);
    }

    public CustomButton(String icon, String text, double width, double height, boolean rounded, String color) {
        this.setPrefWidth(width);
        this.setPrefHeight(height);
        this.setStyle("-fx-background-color: " + color);
        init(icon, text, rounded);
    }

    /**
     * Initializes basic styles for the button
     *
     * @param icon icon string
     * @param text text string
     */
    private void init(String icon, String text, boolean rounded) {
        if(rounded) {
            this.setStyle(getStyle() + "; -fx-background-radius: 25px;");
        }
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        Interaction.setClickable(this, false);
        this.getChildren().addAll(generateIcon(icon), generateText(text));
    }

    /**
     * Generates the Text for the icon
     *
     * @param iconString the string of the icon
     * @return the Text used in the Button
     */
    private Text generateIcon(String iconString) {
        Text icon = new Text(iconString);
        icon.getStyleClass().add("icon");
        return icon;
    }

    /**
     * Generates the Text for the label
     *
     * @param textString label of the button
     * @return the Text used in the Button
     */
    private Text generateText(String textString) {
        Text text = new Text(textString);
        text.getStyleClass().addAll("text", "bold");
        return text;
    }
}
