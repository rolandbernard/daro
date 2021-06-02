package daro.game.ui;

import daro.game.validation.ValidationResult;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ValidationItem extends HBox {

    private ValidationResult result;
    private Text icon, text;

    public ValidationItem(ValidationResult result) {
        this.result = result;
        this.setSpacing(20);
        this.setAlignment(Pos.CENTER);
        icon = new Text();
        text = new Text();
        render();
    }

    private void render() {
        text.setText(result.toString());
        if(result.evaluate()) {
            icon.setText("\ue876");
            this.getStyleClass().remove("failed");
            this.getStyleClass().add("passed");
        } else {
            icon.setText("\ue5cd");
            this.getStyleClass().remove("passed");
            this.getStyleClass().add("failed");
        }
    }

    private void updateResult(ValidationResult result) {
        this.result = result;
        render();
    }
}
