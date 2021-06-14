package daro.game.pages;

import daro.game.io.PlaygroundHandler;
import daro.game.ui.CreateButton;
import daro.game.ui.Heading;
import daro.game.ui.PlaygroundItem;
import daro.game.views.MenuView;
import javafx.scene.layout.VBox;

import java.io.File;

public class PlaygroundPage extends Page implements Reloadable {

    /**
     * <strong>UI: <em>Page</em></strong><br>
     * A page where the user can create daro files to play around with code.
     */
    public PlaygroundPage() {
        Heading heading = new Heading("Playground", "Play around with the language and use what you've learned.");
        getChildren().addAll(heading);
        reload();
    }

    /**
     * Generates PlaygroundItemList
     * 
     * @param playgrounds the playgroundfiles
     * @return Playground
     */
    private VBox getPlaygroundItems(File[] playgrounds) {
        VBox playgroundList = new VBox();
        CreateButton createButton = new CreateButton("Create a new Playground");
        createButton.setOnMouseClicked(e -> MenuView.setContent(new CreatePlaygroundPage()));
        playgroundList.getChildren().add(createButton);
        for (File playground : playgrounds) {
            playgroundList.getChildren().add(new PlaygroundItem(playground, this));
        }
        playgroundList.setSpacing(30);
        return playgroundList;
    }

    @Override
    public void reload() {
        if(getChildren().size() > 1)
            getChildren().remove(1);
        File[] playgrounds = PlaygroundHandler.parsePlaygrounds();
        getChildren().add(getPlaygroundItems(playgrounds));
    }
}
