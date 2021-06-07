package daro.game.pages;

import daro.game.io.UserData;
import daro.game.ui.CustomButton;
import daro.game.ui.Heading;
import daro.game.ui.TextInput;
import daro.game.views.EditorView;
import daro.game.views.MenuView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class CreatePlaygroundPage extends Page {
    private TextInput nameField;
    private CustomButton saveButton;
    private HBox callout;
    private VBox form;
    private Text calloutText;

    public CreatePlaygroundPage() {
        Heading heading = new Heading("Create a new playground", "Give it a name and get started!");
        nameField = new TextInput("Playground name");
        saveButton = new CustomButton("\ue161", "Create the playground", Page.INNER_WIDTH, 60, true);
        saveButton.setOnMouseClicked(this::createPlayground);
        callout = createCallout();
        form = new VBox(nameField, saveButton);
        form.setAlignment(Pos.TOP_RIGHT);
        form.setSpacing(20);

        this.getChildren().addAll(heading, form);
    }

    private void createPlayground(MouseEvent mouseEvent) {
        if(validateNameField()) {
            String error = UserData.createPlayground(nameField.getValue());
            if(error == null) {
                try {
                    this.getScene().setRoot(new EditorView(UserData.getPlaygroundFile(nameField.getValue() + ".daro")));
                } catch (IOException e) {
                    this.getScene().setRoot(new MenuView(new PlaygroundPage()));
                }
            } else {
                form.getChildren().add(0, callout);
                calloutText.setText(error);
            }
        } else {
            form.getChildren().add(0, callout);
            calloutText.setText("A playground's name can only contain letters, numbers and underlines (_).");
        }
    }

    private HBox createCallout() {
        HBox callout = new HBox();
        calloutText = new Text();
        calloutText.getStyleClass().add("text");
        callout.setPadding(new Insets(20));
        callout.setPrefWidth(Page.INNER_WIDTH);
        callout.setStyle("-fx-background-color: #fc323f");
        callout.getChildren().add(calloutText);
        return callout;
    }


    private boolean validateNameField() {
        return nameField.getValue().matches("^[a-zA-Z0-9_]+$");
    }
}
