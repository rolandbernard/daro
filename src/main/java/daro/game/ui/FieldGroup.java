package daro.game.ui;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class FieldGroup extends VBox {
    public FieldGroup(String name, InputField... fields) {
        Text title = new Text(name);
        title.getStyleClass().addAll("heading", "small", "text");
        this.setSpacing(20);
        this.getChildren().add(title);
        this.getChildren().addAll(fields);
    }
}
