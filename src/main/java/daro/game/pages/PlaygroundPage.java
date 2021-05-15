package daro.game.pages;

import daro.game.ui.Heading;

public class PlaygroundPage extends Page {

    /**
     * <strong>UI: <em>Page</em></strong><br>
     * Work in progress
     */
    public PlaygroundPage() {
        Heading heading = new Heading("Playground", "Work in Progress.");
        this.getChildren().add(heading);
    }
}
