package daro.game.pages;

import daro.game.ui.Page;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PlaygroundPage extends Page {

    public PlaygroundPage() {
        this.getChildren().add(new Rectangle(200, 1000, Color.BLACK));
    }
}
