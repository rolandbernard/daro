package daro.game.pages;

import daro.game.main.UserData;
import daro.game.ui.CustomButton;
import daro.game.ui.Heading;
import daro.game.views.EditorView;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class CreatePlaygroundPage extends Page {
    private TextField nameField;
    private CustomButton saveButton;
    private HBox callout;
    private Text calloutText;

    public CreatePlaygroundPage() {
        Heading heading = new Heading("Create a new playground", "Give it a name and get started!");
        nameField = new TextField();
        nameField.setStyle("-fx-font-size: 24px; -fx-background: rgba(255, 255, 255, 0.5)");
        saveButton = new CustomButton("\ue161", "Create the playground");
        saveButton.setOnMouseClicked(this::createPlayground);
        this.callout = createCallout();

        this.getChildren().addAll(heading, callout, nameField, saveButton);
    }

    private void createPlayground(MouseEvent mouseEvent) {
        if(validateNameField()) {
            String error = UserData.createPlayground(nameField.getText());
            if(error == null) {
                this.getScene().setRoot(new EditorView());
            } else {
                callout.setVisible(true);
                calloutText.setText(error);
            }
        } else {
            calloutText.setText("A playground's name can only contain letters, numbers and underlines (_).");
        }
    }

    private HBox createCallout() {
        HBox callout = new HBox();
        calloutText = new Text();
        calloutText.getStyleClass().add("text");
        callout.setVisible(false);
        callout.setPadding(new Insets(20));
        callout.setPrefWidth(Page.INNER_WIDTH);
        callout.setStyle("-fx-background-color: #fc323f");
        callout.getChildren().add(calloutText);
        return callout;
    }


    private boolean validateNameField() {
        return nameField.getText().matches("^[a-zA-Z0-9_]+$");
    }
}
