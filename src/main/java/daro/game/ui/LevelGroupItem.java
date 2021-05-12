package daro.game.ui;

import daro.game.main.Game;
import daro.game.main.LevelGroup;
import daro.game.pages.LevelGroupPage;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class LevelGroupItem extends VBox {
    public static double DIMENSION = 220, H_PADDING = 30, BAR_WIDTH = DIMENSION - H_PADDING * 2 - 2;
    private LevelGroup levelGroup;

    public LevelGroupItem(LevelGroup levelGroup) {
        this.levelGroup = levelGroup;
        this.setSpacing(20);
        this.setPrefSize(DIMENSION, DIMENSION);
        this.setStyle("-fx-background-color: #381A90; -fx-background-radius: 25px");
        this.setPadding(new Insets(40, H_PADDING, 30, H_PADDING));
        this.setCursor(Cursor.HAND);
        this.getChildren().addAll(getHeading(), getCompleted());
        this.setOnMouseClicked(event -> {
            Game.setContent(new LevelGroupPage(levelGroup));
        });
    }

    private VBox getHeading() {
        Text heading = new Text(levelGroup.getName());
        heading.getStyleClass().addAll("heading", "small", "text");
        Text description = new Text(levelGroup.getDescription());
        description.getStyleClass().addAll("text");
        VBox group = new VBox(heading, description);
        group.setSpacing(8);
        group.setPrefHeight(90);
        return group;
    }

    private VBox getCompleted() {
        int completed = levelGroup.countCompletedLevels(),
                allLevels = levelGroup.countLevels();

        VBox group = new VBox(getGraph((double) completed / allLevels), getRatioLabel(completed, allLevels));
        group.setSpacing(10);
        return group;
    }

    private StackPane getGraph(double percent) {
        Rectangle border = new Rectangle(DIMENSION - H_PADDING * 2, 8, Color.web("200D56"));
        border.setArcHeight(8);
        border.setArcWidth(8);

        Rectangle bar = new Rectangle(BAR_WIDTH * percent, 6, Color.web("FF3D23"));
        bar.setArcHeight(8);
        bar.setArcWidth(8);
        return new StackPane(border, bar);
    }

    private VBox getRatioLabel(int completed, int allLevels) {
        Text ratio = new Text(completed + "/" + allLevels);
        ratio.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #fff");
        ratio.getStyleClass().add("text");

        Text completedLabel = new Text("Levels completed");
        completedLabel.setStyle("-fx-font-size: 10px; -fx-fill: #eee");
        completedLabel.getStyleClass().add("text");
        return new VBox(ratio, completedLabel);
    }
}
