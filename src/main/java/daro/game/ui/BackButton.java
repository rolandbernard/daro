package daro.game.ui;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class BackButton extends HBox {
    public static double HEIGHT = 30;

    public BackButton(String label) {
        Text iconText = new Text("\ue5c4");
        iconText.getStyleClass().add("icon");

        Text labelText = new Text(label);
        labelText.getStyleClass().add("text");

        getChildren().addAll(iconText, labelText);
        setPrefHeight(HEIGHT);
        setCursor(Cursor.HAND);
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(15);
    }
}
