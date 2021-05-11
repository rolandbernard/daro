package daro.game.ui;

import daro.game.main.GameHelper;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class Page extends VBox {
    public Page() {
        this.setPadding(new Insets(100, 90, 80, 90));
        this.setStyle("-fx-background-color: #200D57");
        this.setPrefWidth(GameHelper.GAME_WIDTH - Navigation.NAVIGATION_WIDTH);
        this.setMinHeight(GameHelper.GAME_HEIGHT );
    }
}
