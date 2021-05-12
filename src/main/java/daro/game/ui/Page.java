package daro.game.ui;

import daro.game.main.Game;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class Page extends VBox {
    public static double H_PADDING = 90,
            WIDTH = Game.WIDTH - Navigation.WIDTH,
            INNER_WIDTH = WIDTH - H_PADDING * 2;

    public Page() {
        this.setPadding(new Insets(100, H_PADDING, 80, H_PADDING));
        this.setStyle("-fx-background-color: #200D57");
        this.setPrefWidth(WIDTH);
        this.setMinHeight(Game.HEIGHT);
    }
}
