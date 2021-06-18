package daro.game.pages;

import daro.game.main.ThemeColor;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

/**
 * <strong>UI: <em>Template</em></strong><br>
 * An abstract class used to create Pages<br>
 * Pages are used to display content in the MenuView.
 *
 * @author Daniel Planötschers
 */
public abstract class Page extends VBox {
    public static final double H_PADDING = 90;

    /**
     * Sets basic stylings for the pages
     */
    public Page() {
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(100, H_PADDING, 80, H_PADDING));
        this.setStyle("-fx-background-color: " + ThemeColor.BACKGROUND);
        this.setSpacing(60);
        this.setFillWidth(true);
    }
}
