package daro.game.ui;

import daro.game.main.Level;
import daro.game.main.ThemeColor;
import daro.game.views.ExerciseView;
import daro.game.views.View;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * <strong>UI: <em>Component</em></strong><br>
 * An overview item, containing basic information about the level. When clicked
 * it changes the scene to the LevelView
 *
 * @author Daniel Planötscher
 */
public class LevelItem extends StackPane {
    private Level level;

    /**
     * @param level the level displayed
     */
    public LevelItem(Level level) {
        this.level = level;
        getChildren().add(getHeading());
        setAlignment(Pos.CENTER_LEFT);
        if (level.isCompleted()) {
            getChildren().add(IconCircle.getCheckIcon(true));
        }
        setOnMouseClicked(event -> View.updateView(this, new ExerciseView(level)));
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

        String description = level.getDescription().length() > 100 ? level.getDescription().substring(0, 100) + "..."
            : level.getDescription();

        Text descriptionText = new Text(description);
        descriptionText.getStyleClass().addAll("text");
        descriptionText.setWrappingWidth(600);
        box.getChildren().addAll(headingText, descriptionText);
        box.setSpacing(10);
        box.setPadding(new Insets(40));
        box.setStyle(
            "-fx-background-radius: 25px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 0, 20, 0, 0);"
                + "-fx-background-color:" + ThemeColor.LIGHT_BACKGROUND + ";"
        );

        if (level.isCompleted()) {
            box.setStyle(box.getStyle() + "-fx-opacity: 0.5;");
        }
        return box;
    }

}
