package daro.game.pages;

import daro.game.main.Level;
import daro.game.main.LevelGroup;
import daro.game.ui.Heading;
import daro.game.ui.LevelGroupItem;
import daro.game.ui.Page;
import javafx.scene.layout.FlowPane;

import java.util.List;

public class CoursePage extends Page {
    public CoursePage() {
        Heading heading = new Heading("Courses", "Learn the fundamentals of programming with the interactive course.");
        this.getChildren().addAll(heading, getLevelGroups());
        this.setSpacing(60);
    }

    private FlowPane getLevelGroups() {
        FlowPane pane = new FlowPane();
        pane.setPrefWidth(Page.INNER_WIDTH);
        double gap = (Page.INNER_WIDTH - (LevelGroupItem.DIMENSION * 3)) / 3;
        pane.setVgap(gap);
        pane.setHgap(gap);
        pane.getChildren().addAll(
                new LevelGroupItem(new LevelGroup("test", "test", List.of(new Level()))),
                new LevelGroupItem(new LevelGroup("test", "test", List.of(new Level()))),
                new LevelGroupItem(new LevelGroup("test", "test", List.of(new Level())))
        );
        return pane;
    }
}
