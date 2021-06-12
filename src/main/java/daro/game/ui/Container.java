package daro.game.ui;

import daro.game.main.Game;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;

public class Container extends StackPane {
    private Popup popup;
    private ScrollPane content;

    /**
     * <strong>UI: <em>Component</em></strong><br>
     * A scrollable container used as a container for pages
     */
    public Container() {
        popup = new Popup();
        content = new ScrollPane();
        this.getChildren().addAll(content, popup);
        content.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        content.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        content.setFitToHeight(true);
        content.setFitToWidth(true);
        content.setStyle("-fx-background-color: " + Game.colorTheme.get("background"));
    }

    public Popup getPopup() {
        return popup;
    }

    public void setContent(Node n) {
        popup.close();
        content.setContent(n);
    }
}
