package daro.game.ui;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * <strong>UI: <em>Component</em></strong><br>
 * A simple Callout used for user interactions (such as saving etc)
 *
 * @author Daniel Planötscher
 */
public class Callout extends StackPane {
    private Text text;
    private VBox content;
    private String textColor = null;
    private StackPane closeBtn;

    /**
     * Generates a default callout
     *
     * @param text  text content of the callout
     * @param color background color
     */
    public Callout(String text, String color) {
        this.text = new Text(text);
        this.text.getStyleClass().add("text");
        setAlignment(Pos.TOP_RIGHT);
        content = new VBox(this.text);
        content.setFillWidth(true);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: " + color);
        getChildren().add(content);
        createCloseBtn();
    }

    /**
     * Generates a default callout but with different text color
     *
     * @param text      text content of the callout
     * @param color     background color
     * @param textColor custom text color
     */
    public Callout(String text, String color, String textColor) {
        this(text, color);
        this.textColor = textColor;
        this.text.setStyle("-fx-fill: " + textColor);
    }

    /**
     * Updates the text in the callout
     *
     * @param text the new text
     */
    public void setText(String text) {
        this.text.setText(text);
    }

    /**
     * Generates the close button for the component
     */
    private void createCloseBtn() {
        Icon icon = new Icon("\ue5cd");
        if (textColor != null) {
            icon.setStyle("-fx-fill:" + textColor);
        }
        closeBtn = new StackPane(icon);
        closeBtn.setAlignment(Pos.CENTER);
        closeBtn.setMaxWidth(30);
        closeBtn.setMaxHeight(30);
        Interaction.setClickable(closeBtn, false);
        getChildren().add(closeBtn);
    }

    /**
     * Updates the event handler of the click on the close button
     *
     * @param handler the routine that should run when closing a callout
     */
    public void setOnClose(EventHandler<MouseEvent> handler) {
        closeBtn.setOnMouseClicked(handler);
    }
}
