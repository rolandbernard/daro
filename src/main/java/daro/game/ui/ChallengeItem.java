package daro.game.ui;

import daro.game.main.Challenge;
import daro.game.main.ThemeColor;
import daro.game.views.LevelView;
import daro.game.views.View;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ChallengeItem extends VBox {

    private final Challenge challenge;

    public ChallengeItem(Challenge challenge) {
        this.challenge = challenge;
        Text name = new Text(challenge.getName());
        name.getStyleClass().addAll("heading", "small", "text");
        Text creator = new Text("Creator: " + challenge.getCreator());
        creator.getStyleClass().add("text");
        this.setStyle(
                "-fx-background-radius: 25px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 0, 20, 0, 0);"
                        + "-fx-background-color: #381A90;"
        );
        setPadding(new Insets(30));
        Interaction.setClickable(this, true);
        getChildren().addAll(name, creator);
        setOnMouseClicked(e -> openChallenge());
        setFillWidth(true);

    }

    private void openChallenge() {
        View.updateView(this, new LevelView(challenge));
    }


}
