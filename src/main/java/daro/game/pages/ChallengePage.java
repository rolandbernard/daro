package daro.game.pages;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import daro.game.io.ChallengeHandler;
import daro.game.io.IOHelpers;
import daro.game.parser.ChallengeParser;
import daro.game.main.Challenge;
import daro.game.main.ThemeColor;
import daro.game.ui.*;
import daro.game.views.ChallengeBuilderView;
import daro.game.views.ExerciseView;
import daro.game.views.MenuView;
import daro.game.views.View;
import javafx.geometry.Pos;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * <strong>UI: <em>Page</em></strong><br>
 * A page that displays the current list of challenges and the gives the
 * possibility to create / import new ones.
 *
 * @author Daniel Planötscher
 */
public class ChallengePage extends Page implements Reloadable {
    private VBox content;
    private VBox challenges;

    /**
     * Creates a standard challenge page
     */
    public ChallengePage() {
        Heading heading = new Heading("Challenges", "Challenge your friends and create your own levels.");

        CreateButton createButton = new CreateButton("Create a new challenge");
        CreateButton importButton = new CreateButton("Import a new challenge");
        createButton.setOnMouseClicked(e -> View.updateView(this, new ChallengeBuilderView()));
        importButton.setOnMouseClicked(e -> importNewChallenge());
        challenges = new VBox();
        challenges.setSpacing(30);
        content = new VBox(createButton, importButton, challenges);
        content.setSpacing(30);
        getImportedChallenges();
        getChildren().addAll(heading, content);
        initDragAndDrop();
    }

    /**
     * The drag and drop event handlers that allow the adding of new challenges via
     * dropping them into the game
     */
    public void initDragAndDrop() {
        setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            event.setDropCompleted(db.hasFiles());
            event.consume();
            if (db.hasFiles()) {
                Optional<File> importFile =
                    db.getFiles().stream().filter(f -> f.getName().endsWith(".json")).findFirst();
                importFile.ifPresent(this::importChallenge);
            }
        });

        setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            } else {
                event.consume();
            }
        });
    }

    /**
     * Opens the file chooser to accept new levels
     */
    public void importNewChallenge() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(this.getScene().getWindow());
        if (file != null) {
            importChallenge(file);
        }
    }

    /**
     * Handles the importing of new challenges (opens popup if the current challenge
     * has a similar one
     *
     * @param file the challenge file you want to import
     */
    private void importChallenge(File file) {
        try {
            String challengeString = IOHelpers.getFileContent(file);
            Challenge newChallenge = ChallengeParser.parse(challengeString, file);
            if (ChallengeHandler.hasSimilar(newChallenge)) {
                openImportWarning(newChallenge, challengeString, file);
            } else {
                File newFile = ChallengeHandler.importChallenge(file);
                newChallenge = ChallengeParser.parse(challengeString, newFile);
                View.updateView(this, new ExerciseView(newChallenge));
            }

        } catch (Exception e) {
            Callout callout = new Callout("Could not import challenge", ThemeColor.RED.toString());
            challenges.getChildren().add(0, callout);
            callout.setOnClose(event -> challenges.getChildren().remove(callout));
        }
    }

    /**
     * Loads the list of all imported Challenges and maps it to its challenge items
     */
    private void getImportedChallenges() {
        List<Challenge> challenges = ChallengeHandler.getImportedChallenges();
        if (challenges != null) {
            challenges.stream().map(c -> new ChallengeItem(c, this)).forEach(c -> this.challenges.getChildren().add(c));
        }
    }

    /**
     * Opens the import warning screen
     *
     * @param newChallenge    the challenge object of the new challenge
     * @param challengeString the json string containing the challenge data
     * @param file            the file object of the new challenge
     */
    private void openImportWarning(Challenge newChallenge, String challengeString, File file) {
        JsonObject challengeJson = JsonParser.parseString(challengeString).getAsJsonObject();
        Text heading = new Text("Error");
        heading.getStyleClass().addAll("heading", "small", "text");
        heading.setTextAlignment(TextAlignment.CENTER);

        Text information = new Text("You already imported a challenge that is very similar.\nWhat do you want to do?");
        information.getStyleClass().add("text");
        information.setTextAlignment(TextAlignment.CENTER);

        CustomButton cancelBtn = new CustomButton("\ue14c", "Cancel", true);
        cancelBtn.setOnMouseClicked(e -> MenuView.getPopup().close());

        CustomButton replaceBtn = new CustomButton("\ue923", "Replace", true);
        replaceBtn.setOnMouseClicked(e -> {
            if (ChallengeHandler.replaceSimilar(newChallenge, challengeJson)) {
                View.updateView(this, new ExerciseView(newChallenge));
            } else {
                MenuView.getPopup().close();
            }
        });
        CustomButton importBtn = new CustomButton("\ue255", "Import anyway", true);
        importBtn.setOnMouseClicked(e -> {
            File newFile = ChallengeHandler.importChallenge(file);
            Challenge c = ChallengeParser.parse(challengeString, newFile);
            View.updateView(this, new ExerciseView(c));
        });
        HBox buttons = new HBox(cancelBtn, replaceBtn, importBtn);
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.CENTER);
        information.setTextAlignment(TextAlignment.CENTER);
        VBox popupContent = new VBox(heading, information, buttons);
        popupContent.setAlignment(Pos.CENTER);
        popupContent.setSpacing(20);

        MenuView.getPopup().updateContent(popupContent);
        MenuView.getPopup().open();
    }

    @Override
    public void reload() {
        challenges.getChildren().clear();
        getImportedChallenges();
    }
}
