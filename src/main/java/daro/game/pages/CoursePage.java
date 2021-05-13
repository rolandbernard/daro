package daro.game.pages;

import daro.game.main.LevelGroup;
import daro.game.ui.Heading;
import daro.game.ui.LevelGroupItem;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.util.List;


public class CoursePage extends Page {
    /**
     * <strong>UI: <em>Page</em></strong><br>
     * A page that displays the overview of all LevelGroups.
     */
    public CoursePage() {
        Heading heading = new Heading("Courses", "Learn the fundamentals of programming with the interactive course.");
        this.getChildren().addAll(heading, getLevelGroups());
    }

    /**
     * Generates the overview of the level groups
     * @return a FlowPane containing all the level groups
     */
    private FlowPane getLevelGroups() {
        FlowPane pane = new FlowPane();
        pane.setPrefWidth(Page.INNER_WIDTH);

        //The gap between the level groups in the grid
        double gap = (Page.INNER_WIDTH - (LevelGroupItem.DIMENSION * 3)) / 2;
        pane.setVgap(gap);
        pane.setHgap(gap);

        List<LevelGroup> groups = LevelGroup.parseLevels();
        if(groups != null && groups.size() > 0) {
            groups.forEach(group -> pane.getChildren().add(new LevelGroupItem(group)));
        } else {
            Text errorText = new Text("There was an error with loading the level groups.");
            errorText.getStyleClass().add("text");
            pane.getChildren().add(errorText);
        }
        return pane;
    }
}
