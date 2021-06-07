package daro.game.ui;

import daro.game.main.Game;
import daro.game.main.Level;
import daro.game.pages.Page;
import daro.game.views.LevelView;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LevelItem extends VBox {

    private Level level;

    /**
     * <strong>UI: <em>Component</em></strong><br>
     * An overview item, containing basic information about the level. When clicked
     * it changes the scene to the LevelView
     * 
     * @param parentId TODO TOFIX
     * @param level    the level displayed
     */
    public LevelItem(Level level) {
        this.level = level;
        this.setWidth(Page.INNER_WIDTH);
        this.setMinHeight(200);
        this.setStyle(
            "-fx-background-radius: 25px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 0, 20, 0, 0);" +
                    "-fx-background-color:" + Game.colorTheme.get("lightBackground")
        );
        this.getChildren().add(getHeading());
        this.setPadding(new Insets(40));
        this.setOnMouseClicked(event -> this.getScene().setRoot(new LevelView(level)));
        Interaction.setClickable(this, true);
    }


    /**
     * Generates the the heading for the level item
     * 
     * @return a vertical box containing the heading and its description
     */
    private VBox getHeading() {
        VBox box = new VBox();
        Text headingText = new Text(level.getName());
        headingText.getStyleClass().addAll("heading", "small", "text");

        Text descriptionText = new Text(level.getDescription());
        descriptionText.getStyleClass().addAll("text");
        box.getChildren().addAll(headingText, descriptionText);
        box.setSpacing(10);
        return box;
    }

}
