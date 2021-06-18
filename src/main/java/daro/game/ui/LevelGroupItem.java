package daro.game.ui;

import daro.game.main.LevelGroup;
import daro.game.main.ThemeColor;
import daro.game.pages.LevelGroupPage;
import daro.game.views.MenuView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * <strong>UI: <em>Component</em></strong><br>
 * A grid item that displays basic information about a level group (name,
 * description, completed levels) When clicked it leads to the level group
 * detail page
 *
 * @author Daniel Planötscher
 */
public class LevelGroupItem extends VBox {
    public static double DIMENSION = 235, H_PADDING = 30, INNER_WIDTH = DIMENSION - H_PADDING * 2;
    private LevelGroup levelGroup;

    /**
     * Generates a basic level group page
     *
     * @param levelGroup the level group that is displayed
     */
    public LevelGroupItem(LevelGroup levelGroup) {
        this.levelGroup = levelGroup;
        setSpacing(20);
        setMaxSize(DIMENSION, DIMENSION);
        setMinSize(DIMENSION, DIMENSION);
        setAlignment(Pos.CENTER);
        setStyle(
            "-fx-background-radius: 25px;  -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 0, 20, 0, 0);"
                + "-fx-background-color: " + ThemeColor.LIGHT_BACKGROUND
        );
        setPadding(new Insets(H_PADDING));
        getChildren().addAll(getHeading(), getCompleted());
        setOnMouseClicked(event -> MenuView.setContent(new LevelGroupPage(levelGroup)));
        Interaction.setClickable(this, true);
    }

    /**
     * The top part of the item, containing the name and the description of the
     * level group
     *
     * @return a vertical box with the information
     */
    private VBox getHeading() {
        Text heading = new Text(levelGroup.getName());
        heading.getStyleClass().addAll("heading", "small", "text");
        heading.setWrappingWidth(INNER_WIDTH);
        Text description = new Text(levelGroup.getDescription());
        description.setWrappingWidth(INNER_WIDTH);
        description.getStyleClass().addAll("text");
        VBox group = new VBox(heading, description);
        group.setSpacing(8);
        group.setPrefHeight(90);
        return group;
    }

    /**
     * The bottom part of the item, containing information about the completion of a
     * level group
     *
     * @return a vertical box containing a graph and a label
     */
    private VBox getCompleted() {
        int completed = levelGroup.countCompletedLevels(), allLevels = levelGroup.countLevels();

        VBox group = new VBox(getGraph((double)completed / allLevels), getRatioLabel(completed, allLevels));
        group.setSpacing(10);
        return group;
    }

    /**
     * Generates a bar graph
     *
     * @param percent percentage of completion (e.g. 0.5 for 50%)
     * @return a {@link StackPane} containing the graph
     */
    private StackPane getGraph(double percent) {
        Rectangle border = new Rectangle(INNER_WIDTH, 8, Color.web("200D56"));
        border.setArcHeight(8);
        border.setArcWidth(8);

        Rectangle bar = new Rectangle((INNER_WIDTH - 2) * percent, 6, Color.web("FF3D23"));
        bar.setArcHeight(8);
        bar.setArcWidth(8);
        StackPane graph = new StackPane(border, bar);
        graph.setAlignment(Pos.CENTER_LEFT);
        return graph;
    }

    /**
     * Generates a label containing how many levels are completed
     *
     * @param completed amount of completed levels
     * @param allLevels amount of all levels
     * @return a vbox containing the label
     */
    private VBox getRatioLabel(int completed, int allLevels) {
        Text ratio = new Text(completed + "/" + allLevels);
        ratio.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: " + ThemeColor.WHITE);
        ratio.getStyleClass().add("text");

        Text completedLabel = new Text("Levels completed");
        completedLabel.setStyle("-fx-font-size: 10px; -fx-fill: " + ThemeColor.WHITE);
        completedLabel.getStyleClass().add("text");
        return new VBox(ratio, completedLabel);
    }
}
