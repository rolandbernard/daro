package daro.game.ui;

import daro.game.main.Game;
import javafx.scene.control.ScrollPane;


public class Container extends ScrollPane {

    public Container() {
        this.setHeight(Game.HEIGHT);
        this.setPrefWidth(Game.WIDTH - Navigation.WIDTH);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setStyle("-fx-background-color: #200D57");
        this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
    }
}
