package daro.game.pages;

import daro.game.main.LevelGroup;
import daro.game.ui.Heading;
import daro.game.ui.Page;

public class LevelGroupPage extends Page {

    private LevelGroup levelGroup;

    /**
     * <strong>UI: <em>Page</em></strong><br>
     * A detail page for a level group containing a list of the levels.
     * @param levelGroup the level group the page displays
     */
    public LevelGroupPage(LevelGroup levelGroup) {
        this.levelGroup = levelGroup;
        Heading heading = new Heading(levelGroup.getName(), levelGroup.getDescription());
        this.getChildren().addAll(heading);
    }
}
