package daro.game.pages;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import daro.game.io.ChallengeHandler;
import daro.game.main.Challenge;
import daro.game.ui.*;
import daro.game.views.ChallengeBuilderView;
import daro.game.views.LevelView;
import daro.game.views.MenuView;
import daro.game.views.View;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

import java.io.File;
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
        if (file != null) {
            try {
                Scanner scanner = new Scanner(file);
                scanner.useDelimiter("\\Z");
                JsonObject element = JsonParser.parseString(scanner.next()).getAsJsonObject();
                Challenge newChallenge = ChallengeHandler.parseChallenge(element);
                if (ChallengeHandler.hasSimilar(newChallenge)) {
                    Text heading = new Text("Error");
                    heading.getStyleClass().addAll("heading", "small", "text");
                    heading.setTextAlignment(TextAlignment.CENTER);

                    Text information = new Text("You already imported a challenge that is very similar.\nWhat do you want to do?");
                    information.getStyleClass().add("text");
                    information.setTextAlignment(TextAlignment.CENTER);

                    CustomButton cancelBtn = new CustomButton("\ue255", "Cancel", 50, true);
                    cancelBtn.setOnMouseClicked(e -> MenuView.getPopup().close());
                    CustomButton replaceBtn = new CustomButton("\ue923", "Replace", 50, true);
                    replaceBtn.setOnMouseClicked(e -> {
                        if (ChallengeHandler.replaceSimilar(newChallenge, element)) {
                            View.updateView(this, new LevelView(newChallenge));
                        } else {
                            MenuView.getPopup().close();
                        }
                    });
                    CustomButton importBtn = new CustomButton("\ue255", "Import anyway", 50, true);
                    importBtn.setOnMouseClicked(e -> {
                        ChallengeHandler.importChallenge(file);
                        View.updateView(this, new LevelView(newChallenge));
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
                } else {
                    ChallengeHandler.importChallenge(file);
                    View.updateView(this, new LevelView(newChallenge));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void getImportedChallenges() {
        List<Challenge> challenges = ChallengeHandler.getImportedChallenges();
        challenges.stream().map(ChallengeItem::new).forEach(c -> content.getChildren().add(c));
    }

}
