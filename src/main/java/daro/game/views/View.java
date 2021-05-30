package daro.game.views;

import daro.game.main.Game;
import javafx.scene.layout.HBox;

public abstract class View extends HBox {
    /**
     * <strong>UI: <em>Template</em></strong><br>
     * An abstract class used to create Views<br>
     * Views are states of the game, with certain goals (e.g. MenuView has the
     * purpose of showing a menu)
     */
    public View() {
        this.setPrefWidth(Game.WIDTH);
        this.setPrefHeight(Game.HEIGHT);
        this.setStyle("-fx-background-color: #200D57");
    }
}
