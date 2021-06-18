package daro.game.ui;

import daro.game.io.ResourceHandler;
import daro.game.main.ThemeColor;
import daro.game.pages.*;
import daro.game.views.MenuView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;

public class Navigation extends VBox {

    public static final double WIDTH = 320;

    private LinkedHashMap<NavigationItem, Page> navItems;
    private Page defaultPage;

    /**
     * <strong>UI: <em>Component</em></strong><br>
     * The main navigation-sidebar for the game
     *
     * @param defaultPage The default active page
     */
    public Navigation(Page defaultPage) {
        this.setMinWidth(WIDTH);
        this.defaultPage = defaultPage;
        this.setStyle("-fx-background-color: " + ThemeColor.DARK_BACKGROUND);
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(80, 30, 80, 30));
        this.setSpacing(40);
        this.getChildren().addAll(getLogo(), getNavigation());
    }

    /**
     * Generates the logo for the top part of the navigation
     *
     * @return {@link ImageView} containing the logo
     */
    private ImageView getLogo() {
        ImageView logo = new ImageView(ResourceHandler.getImage("logo.png"));
        logo.setFitHeight(40);
        logo.setFitWidth(104);
        logo.setPreserveRatio(true);
        return logo;
    }

    /**
     * Generates the navigation item and links them to the pages.
     *
     * @return a vertical box containing the links
     */
    private VBox getNavigation() {
        navItems = new LinkedHashMap<>();
        addNavItem("\ue021", "Course", CoursePage.class);
        addNavItem("\uea26", "Playground", PlaygroundPage.class);
        addNavItem("\uea2c", "Challenges", ChallengePage.class);
        addNavItem("\ue8b8", "Settings", SettingsPage.class);
        navItems.put(new NavigationItem("\ue9ba", "Exit", false), null);
        linkNavLinks();
        VBox navigation = new VBox();
        navigation.getChildren().addAll(navItems.keySet());
        navigation.setSpacing(5);
        return navigation;
    }

    /**
     * Method used to create new NavItems that link to pages. Detects if the
     * defaultPage is the page you want to create and sets it active accordingly
     *
     * @param icon      icon of the navigation item
     * @param label     label of the navigation item
     * @param pageClass the class of the page you want to create
     */
    private void addNavItem(String icon, String label, Class<? extends Page> pageClass) {
        NavigationItem item = new NavigationItem(icon, label, isDefault(pageClass));
        navItems.put(item, getPage(pageClass));
    }

    /**
     * Checks if the default page is an instance of the current page
     *
     * @param currentPage current page that you want to create
     * @return boolean if the current page is a default page
     */
    private boolean isDefault(Class<? extends Page> currentPage) {
        return currentPage.equals(defaultPage.getClass());
    }

    /**
     * Checks if the current default page is the page wanted, else creates a new
     * instance of the class
     *
     * @param currentPage page that is currently created for a nav link
     * @return a page
     */
    private Page getPage(Class<? extends Page> currentPage) {
        try {
            return isDefault(currentPage) ? defaultPage : currentPage.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Links all navigation links in the navItems-Map to the pages they point to.
     */
    private void linkNavLinks() {
        navItems.keySet().forEach(navItem -> {
            navItem.setOnMouseClicked(event -> {
                if (navItems.get(navItem) == null)
                    System.exit(0);
                MenuView.setContent(navItems.get(navItem));
                navItems.keySet().forEach(item -> item.getStyleClass().remove("active"));
                navItem.getStyleClass().add("active");
            });
        });
    }
}
