package daro.game.ui;

import daro.game.main.Challenge;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ChallengeItem extends VBox {

    private final Challenge challenge;

    public ChallengeItem(Challenge challenge) {
        this.challenge = challenge;
        Text name = new Text(challenge.getName());
        Text creator = new Text(challenge.getCreator());
        this.getChildren().addAll(name, creator);
    }
}
