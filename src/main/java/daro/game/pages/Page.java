package daro.game.pages;

import daro.game.main.Game;
import daro.game.ui.Navigation;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public abstract class Page extends VBox {
    public static final double H_PADDING = 90, WIDTH = Game.WIDTH - Navigation.WIDTH, INNER_WIDTH = WIDTH - H_PADDING * 2;

    /**
     * <strong>UI: <em>Component</em></strong><br>
     * An abstract class used to create Pages
     */
    public Page() {
        this.setPadding(new Insets(100, H_PADDING, 80, H_PADDING));
        this.setStyle("-fx-background-color: #200D57");
        this.setSpacing(60);
        this.setPrefWidth(WIDTH);
        this.setMinHeight(Game.HEIGHT);
    }
}
