package daro.game.ui;

import daro.game.main.Game;
import javafx.scene.layout.VBox;

public class Terminal extends VBox {

    /**
     *
     */
    public Terminal() {
        this.setPrefHeight(Game.HEIGHT);
        this.setPrefWidth(360);
        init();
    }

    /**
     * @param width  TODO TOFIX
     * @param height TODO TOFIX
     */
    public Terminal(double width, double height) {
        this.setPrefHeight(height);
        this.setPrefWidth(width);
        init();
    }

    private void init() {
        this.setStyle("-fx-background-color: #1D1F26");
    }
}
