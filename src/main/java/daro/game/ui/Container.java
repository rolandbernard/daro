package daro.game.ui;

import daro.game.main.Game;
import javafx.scene.control.ScrollPane;

public class Container extends ScrollPane {

    /**
     * <strong>UI: <em>Component</em></strong><br>
     * A scrollable container used as a container for pages
     */
    public Container() {
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        this.setFitToWidth(true);
        this.setFitToHeight(true);
        this.setStyle("-fx-background-color: " + Game.colorTheme.get("background"));
    }
}
