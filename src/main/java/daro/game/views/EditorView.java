package daro.game.views;

import daro.game.main.Game;
import daro.game.ui.CodeEditor;
import daro.game.ui.Terminal;

public class EditorView extends View {

    /**
     * A view showing a CodeEditor and a Terminal
     */
    public EditorView() {
        this.getChildren().addAll(new CodeEditor("", 960, Game.HEIGHT), new Terminal(390, Game.HEIGHT));
    }
}
