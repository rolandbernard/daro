package daro.game.ui;

import daro.game.io.PlaygroundHandler;
import daro.game.main.ThemeColor;
import daro.game.pages.Page;
import daro.game.pages.Reloadable;
import daro.game.views.EditorView;
import daro.game.views.MenuView;
import daro.game.views.View;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <strong>UI: <em>Component</em></strong><br>
 * An overview item of a playground, containing basic information about it.
 *
 * @author Daniel Planötscher
 */
public class PlaygroundItem extends StackPane {
    private VBox mainContent;
    private File file;
    private Reloadable parent;

    /**
     * Generates a basic playground item
     *
     * @param file   The file fo the playground
     * @param parent the page it is displayed in
     */
    public PlaygroundItem(File file, Reloadable parent) {
        this.file = file;
        this.parent = parent;
        Text name = new Text(cleanName());
        name.getStyleClass().addAll("heading", "small", "text");
        mainContent = new VBox(name);
        mainContent.setFillWidth(true);
        mainContent.setPadding(new Insets(40));
        mainContent.setStyle(
                "-fx-background-radius: 25px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 0, 20, 0, 0);"
                        + "-fx-background-color: " + ThemeColor.LIGHT_BACKGROUND
        );
        mainContent.setSpacing(10);
        mainContent.setOnMouseClicked(e -> View.updateView(this, new EditorView(file)));
        Interaction.setClickable(this, true);
        VBox attributes = getAttributes(file);
        if (attributes != null) {
            mainContent.getChildren().add(attributes);
        }

        getChildren().addAll(mainContent, deleteButton());
        setAlignment(Pos.TOP_RIGHT);
    }

    /**
     * Gets the clean name of a the playground file
     *
     * @return a string without .daro
     */
    private String cleanName() {
        return file.getName().split("\\.")[0];
    }

    /**
     * Generates a InfoBox, which is a text and an icon containing information about
     * the playground
     *
     * @param icon  the icon string
     * @param label the label text
     * @return the info box as an HBox
     */
    private HBox generateInfoBox(String icon, String label) {
        Icon iconText = new Icon(icon);
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

            String createDateString = formatDate(format, createDate);
            String modifyDateString = formatDate(format, modifyDate);

            VBox attributes = new VBox();
            attributes.setSpacing(10);
            HBox createDateBox = generateInfoBox("\ue924", "Created: " + createDateString);
            HBox modifyDateBox = generateInfoBox("\ue924", "Last modified: " + modifyDateString);

            attributes.getChildren().addAll(modifyDateBox, createDateBox);
            return attributes;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Formats a FileTime as with the given format
     *
     * @param format the desired format
     * @param time   the file time
     * @return a formatted date
     */
    private String formatDate(SimpleDateFormat format, FileTime time) {
        return format.format(new Date(time.toMillis()));
    }

    /**
     * Generates a delete button
     *
     * @return an icon circle with an event handler
     */
    private IconCircle deleteButton() {
        IconCircle button = IconCircle.getDeleteButton(true);
        button.setOnMouseClicked(e -> openConfirmPopup());
        return button;
    }

    /**
     * Opens a confirmation popup if the user is sure to delete a playground
     */
    private void openConfirmPopup() {
        Text heading = new Text("Warning");
        heading.getStyleClass().addAll("heading", "small", "text");

        Text info = new Text("You are trying to delete the playground " + cleanName() + "\nAre you sure?");
        info.getStyleClass().addAll("text");
        info.setTextAlignment(TextAlignment.CENTER);

        CustomButton cancel = new CustomButton("\ue14c", "No", true);
        cancel.setOnMouseClicked(e -> MenuView.getPopup().close());
        CustomButton yes = new CustomButton("\ue5ca", "Yes", true);
        HBox confirmButtons = new HBox(cancel, yes);
        confirmButtons.setSpacing(10);
        confirmButtons.setAlignment(Pos.CENTER);
        yes.setOnMouseClicked(e -> {
            Callout callout;
            if (PlaygroundHandler.removePlayground(file.getName())) {
                callout = new Callout("The playground was successfully deleted.", ThemeColor.GREEN.toString());
            } else {
                callout = new Callout(
                        "The playground could not be deleted, please try again later.", ThemeColor.RED.toString()
                );
            }
            parent.reload();
            MenuView.getPopup().close();
            if (parent instanceof Page) {
                Page p = (Page) parent;
                p.getChildren().add(1, callout);
                callout.setOnClose(event -> p.getChildren().remove(callout));
            }
        });
        VBox popup = new VBox(heading, info, confirmButtons);
        popup.setAlignment(Pos.CENTER);
        popup.setSpacing(20);
        MenuView.getPopup().updateContent(popup);
        MenuView.getPopup().open();
    }
}
