package daro.game.ui;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Heading extends VBox {

    public Heading(String heading, String description) {
        super();
        Text headingText = new Text(heading);
        headingText.getStyleClass().addAll("heading", "large", "text");

        Text descriptionText = new Text(description);
        descriptionText.getStyleClass().addAll("text", "heading-description");
        this.getChildren().addAll(headingText, descriptionText);
        this.setSpacing(20);
    }
}
