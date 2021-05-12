package daro.game.ui;

import daro.game.main.GameHelper;
import daro.game.main.LevelGroup;
import daro.game.pages.LevelGroupPage;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LevelGroupItem extends VBox {
    public static double DIMENSION = 210;
    private LevelGroup levelGroup;

    public LevelGroupItem(LevelGroup levelGroup) {
        this.levelGroup = levelGroup;
        this.setSpacing(20);
        this.setPrefHeight(DIMENSION);
        this.setPrefWidth(DIMENSION);
        this.setStyle("-fx-background-color: #381A90; -fx-background-radius: 25px");
        this.setPadding(new Insets(40, 30, 40, 30));
        this.setCursor(Cursor.HAND);
        this.getChildren().addAll(getHeading(), getCompleted());
        this.setOnMouseClicked(event -> {
            GameHelper.updateContainer(new LevelGroupPage(levelGroup));
        });
    }

    private VBox getHeading() {
        Text heading = new Text(levelGroup.getName());
        heading.getStyleClass().addAll("heading", "small", "text");
        Text description = new Text(levelGroup.getDescription());
        description.getStyleClass().addAll("text");
        VBox group = new VBox(heading, description);
        group.setSpacing(8);
        return group;
    }
    private VBox getCompleted() {
        int completed = levelGroup.countCompletedLevels(),
                allLevels = levelGroup.countLevels();
        Text ratio = new Text(completed + "/" + allLevels);

        VBox group = new VBox(ratio);
        return group;
    }
}
