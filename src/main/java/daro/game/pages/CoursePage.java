package daro.game.pages;

import daro.game.ui.Page;
import javafx.scene.text.Text;

public class CoursePage extends Page {
    public CoursePage() {
        Text heading = new Text("Courses");
        heading.getStyleClass().addAll("heading", "large", "text");

        Text description = new Text("Learn the fundamentals of programming with the interactive course.");
        description.getStyleClass().addAll("text", "description");
        this.getChildren().addAll(heading, description);
        this.setSpacing(10);
    }
}
