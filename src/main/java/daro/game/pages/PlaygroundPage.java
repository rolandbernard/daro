package daro.game.pages;

import daro.game.ui.Heading;
import daro.game.ui.PlaygroundItem;
import daro.game.views.MenuView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PlaygroundPage extends Page {
    private final String PLAYGROUNDS_PATH = "./user/playgrounds";

    /**
     * <strong>UI: <em>Page</em></strong><br>
     * A page where the user can create daro files to play around with code.
     */
    public PlaygroundPage() {
        Heading heading = new Heading("Playground", "Play around with the language and use what you've learned.");
        File[] playgrounds = parsePlaygrounds();
        this.getChildren().addAll(heading, getCreateButton(), getPlaygroundItems(playgrounds));
    }

    /**
     * Creates, if it not already exists, the playgrounds folder and parses the files
     * @return an array of playground files
     */
    private File[] parsePlaygrounds() {
        File playgrounds = new File(PLAYGROUNDS_PATH);
        if(!playgrounds.exists()) {
            try {
                Files.createDirectories(Path.of(PLAYGROUNDS_PATH));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return playgrounds.listFiles();
    }

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
        Text text = new Text("add a new playground");
        createButton.setSpacing(10);
        createButton.getChildren().addAll(plus, text);
        createButton.setOnMouseClicked(e -> MenuView.setContent(new CreatePlaygroundPage()));
        this.getStyleClass().addAll("text", "playground-item");
        return createButton;
    }

    /**
     * Handles the creation of a new playground
     * @param mouseEvent
     */
    private void createPlayground(MouseEvent mouseEvent) {
        Path newFile = Path.of(PLAYGROUNDS_PATH + "/newFile.daro");
        try {
            Files.createFile(newFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
