package daro.game.views;

import daro.game.main.Game;
import daro.game.ui.CodeEditor;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ChallengeBuilderView extends View {

    private CodeEditor defaultCode;

    public ChallengeBuilderView() {
        VBox defaultCodeField = createDefaultCodeField();
        this.getChildren().addAll(defaultCodeField);
    }

    private VBox createDefaultCodeField() {
        Text label = new Text("The default code given to user to start with.");
        label.getStyleClass().addAll("text", "heading", "tiny");
        defaultCode = new CodeEditor();
        VBox field = new VBox(label, defaultCode);
        field.setSpacing(10);
        field.setStyle("-fx-background-color: " + Game.colorTheme.get("backgroundDark"));
        return field;
    }
}
