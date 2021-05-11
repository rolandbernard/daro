package daro.game.ui;

import daro.game.main.GameHelper;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;


public class Container extends ScrollPane {

    public Container() {
        this.setPrefHeight(GameHelper.GAME_HEIGHT);
        this.setPrefWidth(GameHelper.GAME_WIDTH - Navigation.NAVIGATION_WIDTH);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setStyle("-fx-background-color: #200D57");
        this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
    }
}
