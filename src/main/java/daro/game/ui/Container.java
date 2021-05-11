package daro.game.ui;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;


public class Container extends ScrollPane {

    public Container() {
        this.setPrefHeight(720);
        this.setPrefWidth(960);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setStyle("-fx-background-color: #200D57");
    }
}
