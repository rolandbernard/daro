package daro.game.pages;

import daro.game.io.PlaygroundHandler;
import daro.game.ui.CreateButton;
import daro.game.ui.Heading;
import daro.game.ui.PlaygroundItem;
import daro.game.views.MenuView;
import javafx.scene.layout.VBox;

import java.io.File;

/**
 * <strong>UI: <em>Page</em></strong><br>
 * A page where the user can create daro files to play around with code.
 *
 * @author Daniel Planötscher
 */
public class PlaygroundPage extends Page implements Reloadable {
    private VBox playgroundList;

    /**
     * Generates a basic PlaygroundPage
     */
    public PlaygroundPage() {
        Heading heading = new Heading("Playground", "Play around with the language and use what you've learned.");
        playgroundList = new VBox();
        playgroundList.setSpacing(30);
        reload();
        getChildren().addAll(heading, playgroundList);
    }

    @Override
    public void reload() {
        File[] playgrounds = PlaygroundHandler.parsePlaygrounds();
        playgroundList.getChildren().clear();
        CreateButton createButton = new CreateButton("Create a new Playground");
        createButton.setOnMouseClicked(e -> MenuView.setContent(new CreatePlaygroundPage()));
        playgroundList.getChildren().add(createButton);
        for (File playground : playgrounds) {
            playgroundList.getChildren().add(new PlaygroundItem(playground, this));
        }
    }
}
