package daro.game.ui;

import daro.game.main.Game;
import daro.game.io.PathHandler;
import daro.game.pages.CoursePage;
import daro.game.pages.Page;
import daro.game.pages.PlaygroundPage;
import daro.game.pages.SettingsPage;
import daro.game.views.MenuView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import jdk.jfr.EventSettings;

import java.util.LinkedHashMap;

public class Navigation extends VBox {

    public static final double WIDTH = 320;

    private LinkedHashMap<NavigationItem, Page> navItems;

    /**
     * <strong>UI: <em>Component</em></strong><br>
     * The main navigation-sidebar for the game
     *
     * @param defaultPage The default active page
     */
    public Navigation(Page defaultPage) {
        this.setPrefHeight(Game.HEIGHT);
        this.setPrefWidth(WIDTH);
        this.setStyle("-fx-background-color: " + Game.colorTheme.get("darkBackground"));
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(80, 30, 80, 30));
        this.setSpacing(40);
        this.getChildren().addAll(getLogo(), getNavigation(defaultPage));
    }

    /**
     * Generates the logo for the top part of the navigation
     *
     * @return imageview containing the logo
     */
    private ImageView getLogo() {
        ImageView logo = new ImageView(PathHandler.getImage("logo.png"));
        logo.setFitHeight(40);
        logo.setFitWidth(104);
        logo.setPreserveRatio(true);
        return logo;
    }

    /**
     * Generates the navigation item and links them to the pages.
     *
     * @param defaultPage defaultPage of the navigation
     * @return a vertical box containing the links
     */
    private VBox getNavigation(Page defaultPage) {
        navItems = new LinkedHashMap<>();
        addNavItem("\ue021", "Course", defaultPage, CoursePage.class);
        addNavItem("\uea26", "Playground", defaultPage, PlaygroundPage.class);
        addNavItem("\ue8b8", "Settings", defaultPage, SettingsPage.class);
        navItems.put(new NavigationItem("\ue9ba", "Exit", false), null);
        linkNavLinks();
        VBox navigation = new VBox();
        navigation.getChildren().addAll(navItems.keySet());
        navigation.setSpacing(5);
        return navigation;
    }
    private void addNavItem(String icon, String label, Page defaultPage, Class<? extends Page> pageClass) {
        NavigationItem item = new NavigationItem(icon, label, isDefault(pageClass, defaultPage));
        navItems.put(item, getPage(pageClass, defaultPage));
    }

    /**
     * Checks if the default page is an instance of the current page
     *
     * @param currentPage current page that you want to create
     * @param defaultPage the default page of the navigation
     * @return boolean if the current page is a default page
     */
    private boolean isDefault(Class<? extends Page> currentPage, Page defaultPage) {
        return currentPage.equals(defaultPage.getClass());
    }

    /**
     * Checks if the current default page is the page wanted, else creates a new instance of the class
     *
     * @param currentPage page that is currently created for a nav link
     * @param defaultPage the defaultpage for the navigation
     * @return a page
     */
    private Page getPage(Class<? extends Page> currentPage, Page defaultPage) {
        try {
            return isDefault(currentPage, defaultPage)
                    ? defaultPage
                    : currentPage.getDeclaredConstructor().newInstance();
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
