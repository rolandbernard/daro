package daro.game.ui;

import daro.game.main.ThemeColor;
import daro.game.validation.ValidationResult;
import javafx.geometry.Insets;
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
        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        icon = new Icon();
        icon.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        Text heading = new Text(result.getName());
        heading.getStyleClass().addAll("text", "heading", "tiny");
        text = new Text();
        text.getStyleClass().add("monospace");
        text.setStyle("-fx-font-size: 16px;");
        VBox texts = new VBox(heading, text);
        setPadding(new Insets(24));
        getChildren().addAll(icon, texts);
        setStyle("-fx-background-radius: 15px;");
        render();
    }

    private void render() {
        text.setText(result.toString());
        if (result.evaluate()) {
            icon.setText("\ue876");
            setStyle(getStyle() + "-fx-background-color: " + ThemeColor.GREEN);
        } else {
            icon.setText("\ue5cd");
            setStyle(getStyle() + "-fx-background-color: " + ThemeColor.RED);
        }
    }
}
