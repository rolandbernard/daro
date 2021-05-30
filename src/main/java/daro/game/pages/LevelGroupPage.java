package daro.game.pages;

import daro.game.main.LevelGroup;
import daro.game.ui.Heading;
import daro.game.ui.LevelItem;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LevelGroupPage extends Page {

    private LevelGroup levelGroup;

    /**
     * <strong>UI: <em>Page</em></strong><br>
     * A detail page for a level group containing a list of the levels.
     * 
     * @param levelGroup the level group the page displays
     */
    public LevelGroupPage(LevelGroup levelGroup) {
        this.levelGroup = levelGroup;
        Heading heading = new Heading(levelGroup.getName(), levelGroup.getDescription());
        this.getChildren().addAll(heading, getLevelList());
    }

    /**
     * Generates the list of links to the levels
     * 
     * @return a vertical box containing the levels
     */
    private VBox getLevelList() {
        VBox list = new VBox();
        list.setSpacing(20);
        if (levelGroup.getLevels() != null && levelGroup.getLevels().size() > 0) {
            levelGroup.getLevels().forEach(level -> list.getChildren().add(new LevelItem(level)));
        } else {
            Text errorText = new Text("There was an error with loading the levels.");
            errorText.getStyleClass().add("text");
            list.getChildren().add(errorText);
        }
        return list;
    }
}
