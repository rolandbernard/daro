package daro.game.ui;

import javafx.scene.text.Text;

public class Icon extends Text {

    public Icon() {
        init();
    }

    public Icon(String icon) {
        super(icon);
        init();
    }

    private void init() {
        getStyleClass().add("icon");
    }

}
