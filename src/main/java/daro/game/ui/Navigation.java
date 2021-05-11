package daro.game.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;


public class Navigation extends VBox {

    private ArrayList<HBox> navItems;

    public Navigation() {
        this.setPrefHeight(720);
        this.setPrefWidth(320);
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
        navItems = new ArrayList<>();
        navItems.add(getNavItem("\ue021", "Course"));
        navItems.add(getNavItem("\uea26", "Playground"));
        navItems.add(getNavItem("\ue9ba", "Exit"));
        VBox navigation = new VBox();
        navItems.forEach(item -> navigation.getChildren().add(item));
        navigation.setSpacing(5);
        return navigation;
    }

    private HBox getNavItem(String icon, String label) {
        Text labelText = new Text(label);
        labelText.getStyleClass().addAll("nav-text", "text");

        Text iconText = new Text(icon);
        iconText.getStyleClass().addAll("nav-text", "icon");

        HBox navItem = new HBox(iconText, labelText);
        navItem.setAlignment(Pos.CENTER_LEFT);
        navItem.setPadding(new Insets(10));
        navItem.setSpacing(20);
        navItem.setPrefWidth(240);
        return navItem;
    }
}
