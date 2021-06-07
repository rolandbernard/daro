package daro.game.pages;

import daro.game.io.UserData;
import daro.game.main.Game;
import daro.game.ui.Heading;
import daro.game.ui.Interaction;
import daro.game.ui.PlaygroundItem;
import daro.game.views.MenuView;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;

public class PlaygroundPage extends Page {

    /**
     * <strong>UI: <em>Page</em></strong><br>
     * A page where the user can create daro files to play around with code.
     */
    public PlaygroundPage() {
        Heading heading = new Heading("Playground", "Play around with the language and use what you've learned.");
        File[] playgrounds = UserData.parsePlaygrounds();
        this.getChildren().addAll(heading, getPlaygroundItems(playgrounds));
    }

    /**
     * Generates PlaygroundItemList
     * 
     * @param playgrounds the playgroundfiles
     * @return Playground
     */
    private FlowPane getPlaygroundItems(File[] playgrounds) {
        FlowPane playgroundList = new FlowPane();
        VBox createButton = getCreateButton();
        playgroundList.getChildren().add(createButton);
        for (File playground : playgrounds) {
            playgroundList.getChildren().add(new PlaygroundItem(playground));
        }
        playgroundList.setHgap(PlaygroundItem.MARGIN);
        playgroundList.setVgap(PlaygroundItem.MARGIN);
        return playgroundList;
    }

    /**
     * Generates a create button for new playgrounds.
     * 
     * @return a VBox containing the button
     */
    private VBox getCreateButton() {
        VBox createButton = new VBox();
        Text plus = new Text("\ue147");
        plus.getStyleClass().add("icon");
        createButton.setSpacing(10);
        createButton.getChildren().addAll(plus);
        createButton.setPrefWidth(Page.INNER_WIDTH);
        createButton.setPrefHeight(60);
        createButton
            .setStyle("-fx-background-radius: 15px; -fx-background-color: " + Game.colorTheme.get("lightBackground"));
        createButton.setOnMouseClicked(e -> MenuView.setContent(new CreatePlaygroundPage()));
        Interaction.setClickable(createButton, true);
        createButton.setCursor(Cursor.HAND);
        createButton.setAlignment(Pos.CENTER);
        return createButton;
    }
}
