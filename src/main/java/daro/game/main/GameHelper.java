package daro.game.main;

import daro.game.ui.Container;
import javafx.scene.Node;

public class GameHelper {
    private static Container gameContainer = new Container();

    public static void updateContainer(Node n) {
        gameContainer.setContent(n);
    }

    public static Container getContainer() {
        return gameContainer;
    }
}
