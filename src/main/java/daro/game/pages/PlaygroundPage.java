package daro.game.pages;

import daro.game.ui.Heading;
import daro.game.ui.Page;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PlaygroundPage extends Page {

    public PlaygroundPage() {
        Heading heading = new Heading("Playground", "Work in Progress.");
        this.getChildren().add(heading);
    }
}
