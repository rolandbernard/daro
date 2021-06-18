package daro.game.ui;

import daro.game.main.ThemeColor;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;

/**
 * <strong>UI: <em>Component</em></strong><br>
 * A component representing scrollable container, usually used for pages.
 * Includes also an accessible popup
 *
 * @author Daniel Planötscher
 */
public class Container extends StackPane {
    private Popup popup;
    private ScrollPane content;

    /**
     * Generates a basic container
     */
    public Container() {
        popup = new Popup();
        content = new ScrollPane();
        this.getChildren().addAll(content, popup);
        content.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        content.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        content.setFitToHeight(true);
        content.setFitToWidth(true);
        content.setStyle("-fx-background-color: " + ThemeColor.BACKGROUND);
    }

    public Popup getPopup() {
        return popup;
    }

    /**
     * Updates the content of a container
     *
     * @param node the new content
     */
    public void setContent(Node node) {
        popup.close();
        content.setContent(node);
    }
}
