package daro.game.ui;

import daro.game.main.ThemeColor;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class CustomButton extends HBox {
    public static double HEIGHT = 50;

    /**
     * Creates a custom button with an icon in orange
     *
     * @param icon    icon string e.g. \ue037 
     * @param text    text for the button
     * @param rounded if the button is rounded
     */
    public CustomButton(String icon, String text, boolean rounded) {
        this(icon, text, rounded, ThemeColor.ACCENT.toString());
    }

    /**
     * Creates a custom button with an icon
     *
     * @param icon    icon string e.g. \ue037 
     * @param text    text for the button
     * @param color   custom color
     * @param rounded if the button is rounded
     */
    public CustomButton(String icon, String text, boolean rounded, String color) {
        this.setMinHeight(HEIGHT);
        this.setStyle("-fx-background-color: " + color + ";");
        this.setPadding(new Insets(0, 20, 0, 20));
        if (rounded) {
            this.setStyle(getStyle() + "-fx-background-radius: 25px;");
        }
        setOnMouseEntered(e -> setStyle(getStyle() + "-fx-background-color: derive(" + color + ", -10%);"));
        setOnMouseExited(e -> setStyle(getStyle() + "-fx-background-color: " + color + ";"));
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
