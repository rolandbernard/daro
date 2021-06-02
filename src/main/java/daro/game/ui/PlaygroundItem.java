package daro.game.ui;

import daro.game.pages.Page;
import daro.game.views.EditorView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class PlaygroundItem extends VBox {

    public PlaygroundItem(String name) {
        Text text = new Text(name.split("\\.")[0]);
        text.getStyleClass().add("text");
        this.setPrefWidth(Page.INNER_WIDTH);
        this.setPrefHeight(300);
        this.setStyle(
                "-fx-background-radius: 25px; -fx-background-color: #381A90; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 0, 20, 0, 0);");
        this.getChildren().addAll(text);
        this.setOnMouseClicked(e -> this.getScene().setRoot(new EditorView(name)));
    }

}
