package daro.game.ui;

import javafx.scene.text.Text;

/**
 * <strong>UI: <em>Component</em></strong><br>
 * A component setting the default stylings for an icon
 *
 * @author Daniel Planötscher
 */
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
