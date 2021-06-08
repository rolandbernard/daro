package daro.game.pages;

import daro.game.ui.CreateButton;
import daro.game.ui.Heading;
import daro.game.views.ChallengeBuilderView;
import daro.game.views.View;

public class ChallengesPage extends Page {

    public ChallengesPage() {
        Heading heading = new Heading("Challenges",
                "Challenge your friends and create your own levels.");

        CreateButton createButton = new CreateButton("Create a new Challenge");
        createButton.setOnMouseClicked(e -> View.updateView(this, new ChallengeBuilderView()));

        this.getChildren().addAll(heading, createButton);
    }
}
