package daro.game.ui;

import daro.game.main.LevelGroup;
import javafx.geometry.Insets;
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
        this.setPadding(new Insets(40));
        this.getChildren().addAll(getHeading());
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
}
