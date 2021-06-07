package daro.game.ui;

import daro.game.pages.Page;
import daro.game.views.EditorView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlaygroundItem extends VBox {

    public static final double WIDTH = 300;

    public PlaygroundItem(File file) {
        Text name = new Text(file.getName().split("\\.")[0]);
        name.getStyleClass().addAll("heading", "small", "text");
        this.setPrefHeight(200);
        this.setPrefWidth(WIDTH);
        this.setPadding(new Insets(40));
        this.setStyle("-fx-background-radius: 25px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 0, 20, 0, 0);" +
                "-fx-background-color: #381A90;");
        Interaction.setClickable(this, true);
        this.getChildren().addAll(name);
        HBox createDate = getCreateDateInfo(file);
        if (createDate != null) {
            this.getChildren().add(createDate);
        }
        this.setSpacing(10);
        this.setOnMouseClicked(e -> this.getScene().setRoot(new EditorView(file)));
    }

    private HBox generateInfoBox(String icon, String label) {
        Text iconText = new Text(icon);
        iconText.getStyleClass().add("icon");
        Text labelText = new Text(label);
        labelText.getStyleClass().add("text");
        HBox infoBox = new HBox(iconText, labelText);
        infoBox.setSpacing(5);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        return infoBox;
    }

    private HBox getCreateDateInfo(File file) {
        try {
            BasicFileAttributes fileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            FileTime time = fileAttributes.creationTime();

            String pattern = "dd.MM.yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return generateInfoBox("\ue924", simpleDateFormat.format(new Date(time.toMillis())));
        } catch (IOException e) {
            return null;
        }
    }

}
