package daro.game.views;

import daro.game.main.Level;
import daro.game.ui.Heading;

public class LevelView extends View {

    private Level level;

    /**
     * <strong>UI: <em>View</em></strong><br>
     * A view to display and solve levels.
     */
    public LevelView(Level level) {
        this.level = level;
        this.getChildren().add(new Heading(level.getName(), level.getDescription()));
    }
}
