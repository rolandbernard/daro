package daro.game.views;

import daro.game.io.LevelHandler;
import daro.game.main.Level;
import daro.game.io.UserData;
import daro.game.main.Solvable;
import daro.game.main.ThemeColor;
import daro.game.pages.CoursePage;
import daro.game.ui.*;
import daro.game.validation.Validation;
import daro.game.validation.ValidationResult;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.List;

public class LevelView extends View {

    private static final double SIDEBAR_WIDTH = 340;
    private static final double BOX_PADDINGS = 20;

    private Solvable level;
    private CodeEditor editor;
    private Popup popup;

    /**
     * <strong>UI: <em>View</em></strong><br>
     * A view to display and solve levels.
     *
     * @param level the level shown in the view
     */
    public LevelView(Solvable level) {
        this.level = level;
        editor = new CodeEditor(level.getCode());
        HBox mainContent = new HBox(getSidebar(), editor);

        this.popup = new Popup();
        StackPane wholeContent = new StackPane();
        wholeContent.getChildren().addAll(mainContent, popup);
        wholeContent.setAlignment(Pos.CENTER);
        this.getChildren().add(wholeContent);
    }

    private VBox getSidebar() {
        VBox bar = new VBox();
        ScrollPane textBox = createTextBox();
        Terminal terminal = new Terminal(SIDEBAR_WIDTH);

        BackButton backButton = new BackButton("Back to all courses");
        backButton.setOnMouseClicked(e -> {
            save(ValidationResult.evaluate(Validation.run(editor.getText(), level.getTests())));
            View.updateView(this, new MenuView(new CoursePage()));
        });

        CustomButton runButton = new CustomButton("\ue037", "Run in terminal", false);
        CustomButton submitButton = new CustomButton("\ue86c", "Submit your result", false, ThemeColor.ACCENT_DARK.toString());
        runButton.setOnMouseClicked(e -> terminal.update(editor.getText()));
        submitButton.setOnMouseClicked(this::openValidationPopup);

        bar.getChildren().addAll(backButton, textBox, terminal, runButton, submitButton);
        bar.setMinWidth(SIDEBAR_WIDTH);
        bar.setStyle("-fx-background-color: #2b2e3a");
        return bar;
    }

    private ScrollPane createTextBox() {
        Text title = new Text(level.getName());
        title.setWrappingWidth(SIDEBAR_WIDTH - BOX_PADDINGS * 2);
        title.getStyleClass().addAll("text", "heading", "small");

        Text description = new Text(level.getDescription());
        description.getStyleClass().addAll("text");
        description.setWrappingWidth(SIDEBAR_WIDTH - BOX_PADDINGS * 2);

        VBox textBox = new VBox(title, description);
        textBox.setPadding(new Insets(BOX_PADDINGS));
        textBox.setSpacing(10);

        ScrollPane pane = new ScrollPane();
        pane.setStyle("-fx-background-color: #2b2e3a");
        pane.setMinHeight(250);
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setContent(textBox);

        return pane;
    }

    private boolean save(boolean completion) {
        if(level instanceof Level) {
            Level l = (Level) level;
            return UserData.writeLevelData(l.getGroupId(), l.getId(), completion, editor.getText());
        }
        return false;
    }

    private void openValidationPopup(MouseEvent mouseEvent) {
        popup.open();
        List<ValidationResult> results = Validation.run(editor.getText(), level.getTests());
        boolean success = ValidationResult.evaluate(results);
        save(success);

        VBox items = createValidationItems(results);
        Text heading = new Text(success ? "Congratulations!\nYou passed all the tests" : "Ooops! Try again.");
        heading.getStyleClass().addAll("text", "heading", "small");
        heading.setTextAlignment(TextAlignment.CENTER);

        HBox controls = createControlButtons(success);
        VBox popupContent = new VBox();
        popupContent.getChildren().addAll(heading, items, controls);
        popupContent.setSpacing(30);
        popupContent.setPadding(new Insets(40));
        popupContent.setAlignment(Pos.CENTER);
        popupContent.setPrefWidth(Popup.POPUP_WIDTH);
        popup.updateContent(popupContent);
    }

    private VBox createValidationItems(List<ValidationResult> results) {
        VBox items = new VBox();
        items.setSpacing(20);
        items.setAlignment(Pos.CENTER);
        results.stream().map(ValidationItem::new).forEach(v -> items.getChildren().add(v));
        return items;
    }

    private HBox createControlButtons(boolean success) {
        HBox buttons = new HBox();
        CustomButton mainButton = null;
        if (success) {
            if(level instanceof Level) {
                Level l = (Level) level;
                Level nextLevel = LevelHandler.getNextLevel(l.getGroupId(), l.getId());
                if (nextLevel != null) {
                    mainButton = new CustomButton("\ue16a", "Next Level", true);
                    mainButton.setOnMouseClicked(e -> View.updateView(this, new LevelView(nextLevel)));
                }
            }
        } else {
            mainButton = new CustomButton("\ue5d5", "Try again", true);
            mainButton.setOnMouseClicked(e -> popup.close());
        }
        CustomButton backButton = new CustomButton("\ue5c4", "Back to overview", true);
        backButton.setOnMouseClicked(e -> backToOverview());
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(20);
        buttons.getChildren().add(backButton);
        if (mainButton != null) {
            buttons.getChildren().add(mainButton);
        }
        return buttons;
    }

    private void backToOverview() {
        View.updateView(this, new MenuView(new CoursePage()));
    }

}
