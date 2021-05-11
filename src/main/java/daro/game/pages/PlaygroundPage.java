package daro.game.pages;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class PlaygroundPage extends VBox {

    public PlaygroundPage() {
        this.getChildren().add(new Rectangle(200, 1000, Color.BLACK));
        this.setPadding(new Insets(100, 80, 0, 80));
        this.setStyle("-fx-background-color: #200D57");
        this.setPrefWidth(960);
    }
}
