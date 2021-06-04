package daro.game.ui;

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
     * @param dark   if the background is dark
     */
    public CustomButton(String icon, String text, double width, double height, boolean dark) {
        this.setPrefWidth(width);
        this.setPrefHeight(height);
        init(icon, text, dark);
    }

    /**
     * Creates a custom button with an icon
     *
     * @param icon icon string e.g. \ue037 
     * @param text text for the button
     * @param dark if the background is dark
     */
    public CustomButton(String icon, String text, boolean dark) {
        this.setPadding(new Insets(15));
        this.setStyle("-fx-background-radius: 5px;");
        init(icon, text, dark);
    }

    /**
     * Initializes basic stylings for the button
     *
     * @param icon icon string
     * @param text text string
     */
    private void init(String icon, String text, boolean dark) {
        if (dark) {
            this.setStyle("-fx-background-color: #dd331c");
        } else {
            this.setStyle("-fx-background-color: #FF3D23");
        }
        this.setAlignment(Pos.CENTER);
        this.setCursor(Cursor.HAND);
        this.setSpacing(10);
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
