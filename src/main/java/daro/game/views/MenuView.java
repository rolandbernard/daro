package daro.game.views;

import daro.game.pages.Page;
import daro.game.ui.Container;
import daro.game.ui.Navigation;
import daro.game.ui.Popup;
import javafx.scene.layout.Priority;

/**
 * <strong>UI: <em>View</em></strong><br>
 * A view with a navigation sidebar and pages.
 *
 * @author Daniel Planötscher
 */
public class MenuView extends View {
    private final static Container CONTENT = new Container();

    /**
     * Creates a new MenuView with a starting page
     *
     * @param defaultPage the starting page
     */
    public MenuView(Page defaultPage) {
        setContent(defaultPage);
        setHgrow(CONTENT, Priority.ALWAYS);
        this.getChildren().addAll(new Navigation(defaultPage), CONTENT);
    }

    /**
     * Updates the main content of the MenuView to a given page.
     *
     * @param page The page the content should contain
     */
    public static void setContent(Page page) {
        CONTENT.setContent(page);
    }

    /**
     * Returns the popup, that can be used in MenuViews.
     *
     * @return the popup
     */
    public static Popup getPopup() {
        return CONTENT.getPopup();
    }
}
