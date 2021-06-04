package daro.game.views;

import daro.game.pages.Page;
import daro.game.ui.Container;
import daro.game.ui.Navigation;

public class MenuView extends View {
    private final static Container CONTENT = new Container();

    /**
     * <strong>UI: <em>View</em></strong><br>
     * A view with a navigation sidebar and pages.
     * 
     * @param defaultPage the starting page
     */
    public MenuView(Page defaultPage) {
        setContent(defaultPage);
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
}
