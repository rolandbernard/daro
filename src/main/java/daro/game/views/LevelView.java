package daro.game.views;

import daro.game.main.Level;
import daro.game.ui.CodeEditor;

public class LevelView extends View {

    private Level level;

    /**
     * <strong>UI: <em>View</em></strong><br>
     * A view to display and solve levels.
     * 
     * @param level TODO TOFIX
     */
    public LevelView(Level level) {
        this.level = level;
        this.getChildren().addAll(new CodeEditor());
    }
}
