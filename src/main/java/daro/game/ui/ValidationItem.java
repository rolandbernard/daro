package daro.game.ui;

import daro.game.validation.ValidationResult;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ValidationItem extends HBox {

    private ValidationResult result;
    private Text text;
    private Icon icon;

    public ValidationItem(ValidationResult result) {
        this.result = result;
        this.setSpacing(5);
        this.setAlignment(Pos.CENTER);
        icon = new Icon();
        Text heading = new Text(result.getName());
        heading.getStyleClass().addAll("text", "heading", "tiny");
        text = new Text();
        text.getStyleClass().add("text");
        text.setWrappingWidth(340);
        VBox texts = new VBox(heading, text);
        texts.setSpacing(5);
        this.getChildren().addAll(icon, texts);
        render();
    }

    private void render() {
        text.setText(result.toString());
        if (result.evaluate()) {
            icon.setText("\ue876");
            this.getStyleClass().remove("failed");
            this.getStyleClass().add("passed");
        } else {
            icon.setText("\ue5cd");
            this.getStyleClass().remove("passed");
            this.getStyleClass().add("failed");
        }
    }
}
