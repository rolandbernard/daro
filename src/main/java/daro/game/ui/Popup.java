package daro.game.ui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;

/**
 * <strong>UI: <em>Component</em></strong><br>
 * A simple popup that is scrollable
 *
 * @author Daniel Planötscher
 */
public class Popup extends StackPane {
    private ScrollPane wrapper;
    private StackPane content;
    public static final double POPUP_WIDTH = 640;

    /**
     * Generates a basic popup
     */
    public Popup() {
        content = new StackPane();
        content.setAlignment(Pos.CENTER);
        wrapper = new ScrollPane(content);
        wrapper.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        wrapper.setMaxWidth(POPUP_WIDTH);
        wrapper.setPrefWidth(POPUP_WIDTH);
        wrapper.setFitToHeight(true);
        wrapper.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        wrapper.setStyle("-fx-background-color: transparent");
        this.setStyle("-fx-background-color: rgba(0,0,0,0.75)");
        this.setVisible(false);
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(wrapper);
    }

    /**
     * Generates a popup with predefined content
     *
     * @param content the content
     */
    public Popup(Node content) {
        this();
        updateContent(content);
    }

    /**
     * Updates the content of a popup
     *
     * @param content the new content
     */
    public void updateContent(Node... content) {
        this.content.getChildren().clear();
        this.content.getChildren().addAll(content);
    }

    /**
     * Opens a popup
     */
    public void open() {
        Interaction.scaleIn(content);
        this.setVisible(true);
    }

    /**
     * Closes a popup
     */
    public void close() {
        this.setVisible(false);
    }
}
