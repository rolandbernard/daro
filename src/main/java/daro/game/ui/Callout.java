package daro.game.ui;

import daro.game.pages.Page;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class Callout extends HBox {

    private Text text;
    public Callout(String text, String color) {
        this.text = new Text(text);
        this.text.getStyleClass().add("text");
        this.setPadding(new Insets(20));
        this.setPrefWidth(Page.INNER_WIDTH);
        this.setStyle("-fx-background-color: " + color);
        this.getChildren().add(this.text);
    }

    public Callout(String text, String color, String textColor) {
        this(text, color);
        this.text.setStyle("-fx-fill: " + textColor);
    }

    public void setText(String text) {
        this.text.setText(text);
    }
}
