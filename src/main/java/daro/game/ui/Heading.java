package daro.game.ui;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * <strong>UI: <em>Component</em></strong><br>
 * The main heading for a page with a short description
 *
 * @author Daniel Planötscher
 */
public class Heading extends VBox {

    /** Generates a basic heading component
     * @param heading     a string containing main heading (e.g. Course,
     *                    Playground...)
     * @param description a string containing a description, showing information
     *                    about the page
     */
    public Heading(String heading, String description) {
        Text headingText = new Text(heading);
        headingText.getStyleClass().addAll("heading", "large", "text");

        Text descriptionText = new Text(description);
        descriptionText.getStyleClass().addAll("text", "heading-description");
        setFillWidth(true);
        getChildren().addAll(headingText, descriptionText);
        setSpacing(20);
    }
}
