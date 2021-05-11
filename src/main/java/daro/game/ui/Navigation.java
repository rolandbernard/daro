package daro.game.ui;

import daro.game.main.GameHelper;
import daro.game.pages.CoursePage;
import daro.game.pages.PlaygroundPage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.LinkedHashMap;


public class Navigation extends VBox {

    public static final double NAVIGATION_WIDTH = 320;

    private LinkedHashMap<HBox, Page> navItems;

    public Navigation() {
        this.setPrefHeight(GameHelper.GAME_HEIGHT);
        this.setPrefWidth(NAVIGATION_WIDTH);
        this.setStyle("-fx-background-color: #1A0A47");
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(80, 30, 80, 30));
        this.setSpacing(40);
        this.getChildren().addAll(getLogo(), getNavigation());
    }

    private ImageView getLogo() {
        ImageView logo = new ImageView(new Image("img/logo.png"));
        logo.setFitHeight(40);
        logo.setFitWidth(104);
        logo.setPreserveRatio(true);
        return logo;
    }

    private VBox getNavigation() {
        navItems = new LinkedHashMap<>();
        navItems.put(getNavItem("\ue021", "Course", true), new CoursePage());
        navItems.put(getNavItem("\uea26", "Playground", false), new PlaygroundPage());
        navItems.put(getNavItem("\ue9ba", "Exit", false), null);

        VBox navigation = new VBox();
        navigation.getChildren().addAll(navItems.keySet());
        navigation.setSpacing(5);
        return navigation;
    }

    private HBox getNavItem(String icon, String label, boolean isDefault) {
        Text labelText = new Text(label);
        labelText.getStyleClass().addAll("nav-text", "text");

        Text iconText = new Text(icon);
        iconText.getStyleClass().addAll("nav-text", "icon");

        HBox navItem = new HBox(iconText, labelText);
        navItem.setAlignment(Pos.CENTER_LEFT);
        navItem.setSpacing(20);
        navItem.setPrefWidth(240);
        navItem.setCursor(Cursor.HAND);
        navItem.getStyleClass().add("nav-item");
        if(isDefault) {
            navItem.getStyleClass().add("active");
        }

        navItem.setOnMouseClicked(event -> {
            if(navItems.get(navItem) == null)
                System.exit(0);
            GameHelper.updateContainer(navItems.get(navItem));
            navItems.keySet().forEach(item -> item.getStyleClass().remove("active"));
            navItem.getStyleClass().add("active");
        });

        return navItem;
    }
}
