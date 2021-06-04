package daro.game.ui;


import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;

public class Popup extends StackPane {
    private ScrollPane content;
    public static final double POPUP_WIDTH = 640;

    public Popup() {
        init();
    }

    public Popup(Node content) {
        init();
        updateContent(content);
    }

    private void init() {
        this.setStyle("-fx-background-color: rgba(0,0,0,0.5)");
        this.setVisible(false);
        this.setAlignment(Pos.CENTER);
        content = new ScrollPane();
        content.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        content.setMaxHeight(400);
        content.setMaxWidth(POPUP_WIDTH);
        content.setStyle("-fx-background-color: #2b2e3a; -fx-background-radius: 25px;");
        this.getChildren().add(content);
    }

    public void updateContent(Node content) {
        this.content.setContent(content);
    }

    public void open() {
        this.setVisible(true);
    }

    public void close() {
        this.setVisible(false);
    }
}
