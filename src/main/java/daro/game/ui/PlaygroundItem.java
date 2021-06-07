package daro.game.ui;

import daro.game.pages.Page;
import daro.game.views.EditorView;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class PlaygroundItem extends VBox {

    public static double MARGIN = 30;

    public PlaygroundItem(String name) {
        Text text = new Text(name.split("\\.")[0]);
        text.getStyleClass().addAll("heading", "small", "text");
        this.setPrefHeight(200);
        this.setPrefWidth(Page.INNER_WIDTH / 2 - MARGIN * 0.6);
        this.setPadding(new Insets(30));
        this.setStyle("-fx-background-radius: 25px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 0, 20, 0, 0);" +
                        "-fx-background-color: #381A90;");
        Interaction.setClickable(this, true);
        this.getChildren().addAll(text);
        this.setOnMouseClicked(e -> this.getScene().setRoot(new EditorView(name)));
    }

}
