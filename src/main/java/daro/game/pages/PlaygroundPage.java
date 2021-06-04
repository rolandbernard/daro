package daro.game.pages;

import daro.game.io.UserData;
import daro.game.ui.Heading;
import daro.game.ui.PlaygroundItem;
import daro.game.views.MenuView;
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
        VBox createButton = getCreateButton();
        createButton.setLayoutX(20);
        createButton.setLayoutY(20);
        this.getChildren().addAll(heading, createButton, getPlaygroundItems(playgrounds));
    }


    /**
     * Generates PlaygroundItemList
     * @param playgrounds the playgroundfiles
     * @return Playground
     */
    private VBox getPlaygroundItems(File[] playgrounds) {
        VBox playgroundList = new VBox();
        for(File playground : playgrounds) {
            playgroundList.getChildren().add(new PlaygroundItem(playground.getName()));
        }
        playgroundList.setSpacing(20);
        return playgroundList;
    }

    /**
     * Generates a create button for new playgrounds.
     * @return a VBox containing the button
     */
    private VBox getCreateButton() {
        VBox createButton = new VBox();
        Text plus = new Text("+");
        createButton.setSpacing(10);
        createButton.getChildren().addAll(plus);
        createButton.setOnMouseClicked(e -> MenuView.setContent(new CreatePlaygroundPage()));
        this.getStyleClass().addAll("text", "playground-item");
        return createButton;
    }
}
