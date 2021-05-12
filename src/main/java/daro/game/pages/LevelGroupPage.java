package daro.game.pages;

import daro.game.main.LevelGroup;
import daro.game.ui.Heading;
import daro.game.ui.Page;

public class LevelGroupPage extends Page {

    LevelGroup levelGroup;

    public LevelGroupPage(LevelGroup levelGroup) {
        this.levelGroup = levelGroup;
        Heading heading = new Heading(levelGroup.getName(), levelGroup.getDescription());
        this.getChildren().addAll(heading);
    }
}
