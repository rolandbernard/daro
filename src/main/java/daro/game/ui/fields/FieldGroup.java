package daro.game.ui.fields;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class FieldGroup extends VBox {
    private Text title;

    public FieldGroup(String name, InputField... fields) {
        title = new Text(name);
        title.getStyleClass().addAll("heading", "small", "text");
        this.setSpacing(20);
        this.getChildren().add(title);
        this.getChildren().addAll(fields);
    }

    public void setName(String name) {
        title.setText(name);
    }
}
