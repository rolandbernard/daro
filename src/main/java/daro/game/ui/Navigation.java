package daro.game.ui;

import daro.game.main.Game;
import daro.game.pages.Page;
import daro.game.pages.PlaygroundPage;
import daro.game.views.MenuView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

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
        this.setStyle("-fx-background-color: #1A0A47");
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
        ImageView logo = new ImageView(new Image("img/logo.png"));
        logo.setFitHeight(40);
        logo.setFitWidth(104);
        logo.setPreserveRatio(true);
        return logo;
    }

    /**
     * Generates the navigation item and links them to the pages.
     * 
     * @param defaultPage TODO TOFIX
     * @return a vertical box containing the links
     */
    private VBox getNavigation(Page defaultPage) {
        navItems = new LinkedHashMap<>();
        navItems.put(new NavigationItem("\ue021", "Course", true), defaultPage);
        navItems.put(new NavigationItem("\uea26", "Playground", false), new PlaygroundPage());
        navItems.put(new NavigationItem("\ue9ba", "Exit", false), null);
        linkNavLinks();

        VBox navigation = new VBox();
        navigation.getChildren().addAll(navItems.keySet());
        navigation.setSpacing(5);
        return navigation;
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
