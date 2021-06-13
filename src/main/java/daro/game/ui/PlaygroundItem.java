package daro.game.ui;

import daro.game.views.EditorView;
import daro.game.views.View;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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


    /**
     * <strong>UI: <em>Component</em></strong><br>
     * An overview item of a playground, containing basic information about it.
     *
     * @param file The file fo the playground
     */
    public PlaygroundItem(File file) {
        Text name = new Text(file.getName().split("\\.")[0]);
        name.getStyleClass().addAll("heading", "small", "text");
        this.setFillWidth(true);
        this.setPadding(new Insets(40));
        this.setStyle(
            "-fx-background-radius: 25px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 0, 20, 0, 0);"
                + "-fx-background-color: #381A90;"
        );
        Interaction.setClickable(this, true);
        this.getChildren().addAll(name);
        VBox attributes = getAttributes(file);
        if (attributes != null) {
            this.getChildren().add(attributes);
        }
        this.setSpacing(10);
        this.setOnMouseClicked(e -> View.updateView(this, new EditorView(file)));
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

    private VBox getAttributes(File file) {
        try {
            String pattern = "dd.MM.yyyy";
            SimpleDateFormat format = new SimpleDateFormat(pattern);

            BasicFileAttributes fileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            FileTime createDate = fileAttributes.creationTime();
            FileTime modifyDate = fileAttributes.lastModifiedTime();

            String createDateString =  formatDate(format, createDate);
            String modifyDateString =  formatDate(format, modifyDate);

            VBox attributes = new VBox();
            attributes.setSpacing(10);
            HBox createDateBox = generateInfoBox("\ue924","Created: " + createDateString);
            HBox modifyDateBox = generateInfoBox("\ue924","Last modified: " + modifyDateString);

            attributes.getChildren().addAll(modifyDateBox, createDateBox);
            return attributes;
        } catch (IOException e) {
            return null;
        }
    }

    private String formatDate(SimpleDateFormat format, FileTime time) {
        return format.format(new Date(time.toMillis()));
    }

}
