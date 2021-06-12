package daro.game.pages;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import daro.game.io.ChallengeHandler;
import daro.game.main.Challenge;
import daro.game.ui.ChallengeItem;
import daro.game.ui.CreateButton;
import daro.game.ui.Heading;
import daro.game.views.ChallengeBuilderView;
import daro.game.views.LevelView;
import daro.game.views.View;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class ChallengesPage extends Page {
    private VBox content;

    public ChallengesPage() {
        Heading heading = new Heading("Challenges",
                "Challenge your friends and create your own levels.");

        CreateButton createButton = new CreateButton("Create a new challenge");
        CreateButton importButton = new CreateButton("Import a new challenge");
        createButton.setOnMouseClicked(e -> View.updateView(this, new ChallengeBuilderView()));
        importButton.setOnMouseClicked(e -> importNewChallenge());
        content = new VBox(createButton, importButton);
        content.setSpacing(20);
        getImportedChallenges();
        this.getChildren().addAll(heading, content);
    }

    public void importNewChallenge() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(this.getScene().getWindow());
        if(file != null) {
            Path challenge = ChallengeHandler.importChallenge(file);
            if(challenge != null) {
                try {
                    Scanner scanner = new Scanner(challenge);
                    scanner.useDelimiter("\\Z");
                    JsonObject element = JsonParser.parseString(scanner.next()).getAsJsonObject();
                    View.updateView(this, new LevelView(ChallengeHandler.parseChallengeFromJsonObject(element)));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getImportedChallenges() {
        List<Challenge> challenges = ChallengeHandler.getImportedChallenges();
        challenges.stream().map(ChallengeItem::new).forEach(c -> content.getChildren().add(c));
    }

}
