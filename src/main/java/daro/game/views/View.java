package daro.game.views;

import daro.game.main.Game;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public abstract class View extends HBox {
    /**
     * <strong>UI: <em>Template</em></strong><br>
     * An abstract class used to create Views<br>
     * Views are states of the game, with certain goals (e.g. MenuView has the
     * purpose of showing a menu)
     */
    public View() {
        // this.setPrefWidth(Game.WIDTH);
        // this.setPrefHeight(Game.HEIGHT);
        // this.
        this.setFillHeight(true);
        this.setStyle("-fx-background-color: #200D57");
    }

    /**
     * Updates the view of the game
     *
     * @param n       a JavaFX node that is within the View you want to update
     *                (usually 'this')
     * @param newView the new View you want to set the game to
     */
    public static void updateView(Node n, View newView) {
        n.getScene().setRoot(newView);
    }
}
