package daro.game.ui;


import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;

public class Popup extends StackPane {
    private ScrollPane wrapper;
    private StackPane content;
    public static final double POPUP_WIDTH = 640;

    public Popup() {
        content = new StackPane();
        content.setAlignment(Pos.CENTER);
        wrapper = new ScrollPane(content);
        wrapper.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        wrapper.setMaxWidth(POPUP_WIDTH);
        wrapper.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        wrapper.setStyle("-fx-background-color: transparent");
        this.setStyle("-fx-background-color: rgba(0,0,0,0.5)");
        this.setVisible(false);
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(wrapper);
    }

    public Popup(Node content) {
        this();
        updateContent(content);
    }

    public void updateContent(Node content) {
        this.content.getChildren().add(content);
    }

    public void open() {
        Interaction.scaleIn(content);
        this.setVisible(true);
    }

    public void close() {
        this.setVisible(false);
    }
}
