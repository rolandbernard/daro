package daro.game.main;

import daro.game.ui.Container;
import daro.game.ui.Page;

public class GameHelper {
    private final static Container GAME_CONTAINER = new Container();
    public static final double GAME_HEIGHT = 720, GAME_WIDTH = 1280;

    public static void updateContainer(Page n) {
        GAME_CONTAINER.setContent(n);
    }

    public static Container getContainer() {
        return GAME_CONTAINER;
    }
}
