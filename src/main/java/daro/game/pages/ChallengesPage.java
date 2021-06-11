package daro.game.pages;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import daro.game.io.ChallengeHandler;
import daro.game.io.LevelHandler;
import daro.game.main.Challenge;
import daro.game.ui.CreateButton;
import daro.game.ui.Heading;
import daro.game.views.ChallengeBuilderView;
import daro.game.views.LevelView;
import daro.game.views.View;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class ChallengesPage extends Page {

    public ChallengesPage() {
        Heading heading = new Heading("Challenges",
                "Challenge your friends and create your own levels.");

        CreateButton createButton = new CreateButton("Create a new Challenge");
        CreateButton importButton = new CreateButton("Import level");
        createButton.setOnMouseClicked(e -> View.updateView(this, new ChallengeBuilderView()));
        importButton.setOnMouseClicked(e -> openChallenge());

        this.getChildren().addAll(heading, createButton, importButton);
    }

    public void openChallenge() {
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
                    View.updateView(this, new LevelView(LevelHandler.parseChallengeFromJsonObject(element)));

                } catch (Exception e) { }
            }
        }
    }
}
