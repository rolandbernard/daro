package daro.game.ui;

import daro.game.io.ChallengeHandler;
import daro.game.main.Challenge;
import daro.game.main.ThemeColor;
import daro.game.pages.Page;
import daro.game.pages.Reloadable;
import daro.game.views.ExerciseView;
import daro.game.views.MenuView;
import daro.game.views.View;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ChallengeItem extends StackPane {

    private final Challenge challenge;
    private final Reloadable parent;

    public ChallengeItem(Challenge challenge, Reloadable parent) {
        this.parent = parent;
        this.challenge = challenge;
        Text name = new Text(challenge.getName());
        name.getStyleClass().addAll("heading", "small", "text");
        Text creator = new Text("Creator: " + challenge.getCreator());
        creator.getStyleClass().add("text");
        Interaction.setClickable(this, true);
        VBox mainContent = new VBox(name, creator);
        mainContent.setFillWidth(true);
        mainContent.setPadding(new Insets(40));
        mainContent.setOnMouseClicked(e -> openChallenge());
        mainContent.setStyle(
                "-fx-background-radius: 25px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 0, 20, 0, 0);"
                        + "-fx-background-color: " + ThemeColor.LIGHT_BACKGROUND
        );
        StackPane itemWrapper = new StackPane(mainContent, deleteButton());
        itemWrapper.setAlignment(Pos.TOP_RIGHT);
        getChildren().add(itemWrapper);
        if(challenge.isCompleted()) {
            itemWrapper.setStyle("-fx-opacity: 0.5");
            getChildren().add(checkCircle());
        }
        setAlignment(Pos.CENTER_LEFT);
    }

    private void openChallenge() {
        View.updateView(this, new ExerciseView(challenge));
    }

    private IconCircle deleteButton() {
        IconCircle button = IconCircle.getDeleteButton(true);
        button.setOnMouseClicked(e -> openConfirmPopup());
        return button;
    }

    private IconCircle checkCircle() {
        return IconCircle.getCheckIcon(true);
    }

    private void openConfirmPopup() {
        Text heading = new Text("Warning");
        heading.getStyleClass().addAll("heading", "small", "text");

        Text info = new Text("You are trying to delete the the challenge " + challenge.getName() + "\nAre you sure?");
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
            if(ChallengeHandler.removeChallenge(challenge.getSourceFile())) {
                callout = new Callout("The challenge was successfully deleted.", ThemeColor.GREEN.toString());
            } else {
                callout = new Callout("The challenge could not be deleted, please try again later.", ThemeColor.RED.toString());
            }
            parent.reload();
            MenuView.getPopup().close();
            if(parent instanceof Page) {
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
