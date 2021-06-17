package daro.game.ui;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Callout extends StackPane {
    private Text text;
    private VBox content;
    private String textColor = null;
    private StackPane closeBtn;

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

    public Callout(String text, String color, String textColor) {
        this(text, color);
        this.textColor = textColor;
        this.text.setStyle("-fx-fill: " + textColor);
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    private void createCloseBtn() {
        Text icon = new Text("\ue5cd");
        icon.getStyleClass().add("icon");
        if (textColor != null) {
            icon.setStyle("-fx-fill:" + textColor);
        }
        closeBtn = new StackPane(icon);
        closeBtn.setAlignment(Pos.CENTER);
        closeBtn.setMaxWidth(30);
        closeBtn.setMaxHeight(30);
        Interaction.setClickable(closeBtn, false);
        this.getChildren().add(closeBtn);
    }

    public void setOnClose(EventHandler<MouseEvent> handler) {
        closeBtn.setOnMouseClicked(handler);
    }
}
