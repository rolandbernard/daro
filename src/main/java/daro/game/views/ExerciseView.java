package daro.game.views;

import daro.game.io.ChallengeHandler;
import daro.game.io.LevelHandler;
import daro.game.main.Challenge;
import daro.game.main.Level;
import daro.game.main.Exercise;
import daro.game.main.ThemeColor;
import daro.game.pages.ChallengePage;
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

/**
 * <strong>UI: <em>View</em></strong><br>
 * A view to display and solve levels.
 *
 * @author Daniel Planötscher
 */
public class ExerciseView extends View {
    private static final double SIDEBAR_WIDTH = 380;
    private static final double BOX_PADDINGS = 20;

    private Exercise exercise;
    private CodeEditor editor;
    private Popup popup;

    /**
     * Generates a basic exercise view
     *
     * @param exercise the exercise shown in the view
     */
    public ExerciseView(Exercise exercise) {
        this.exercise = exercise;
        editor = new CodeEditor(exercise.getCode());
        HBox mainContent = new HBox(getSidebar(), editor);

        popup = new Popup();
        StackPane wholeContent = new StackPane();
        wholeContent.getChildren().addAll(mainContent, popup);
        wholeContent.setAlignment(Pos.CENTER);
        getChildren().add(wholeContent);
    }

    /**
     * Generates the sidebar containing the task, terminal and controls.
     *
     * @return a VBox with the sidebar
     */
    private VBox getSidebar() {
        VBox bar = new VBox();
        ScrollPane textBox = createTextBox();
        Terminal terminal = new Terminal(SIDEBAR_WIDTH);
        BackButton backButton = new BackButton("Back to overview");
        backButton.setOnMouseClicked(e -> {
            if (save(ValidationResult.evaluate(Validation.run(editor.getText(), exercise.getTests())))) {
                backToOverview();
            }
        });
        HBox controlsBox = new HBox(backButton);
        controlsBox.setPadding(new Insets(BOX_PADDINGS, 0, 0, BOX_PADDINGS));
        if (exercise instanceof Level) {
            Level l = (Level) exercise;
            if (l.getHelpCode() != null || l.getHelpText() != null) {
                controlsBox.setSpacing(30);
                controlsBox.getChildren().add(getHelpButton());
                controlsBox.setAlignment(Pos.CENTER_LEFT);
            }
        }

        CustomButton runButton = new CustomButton("\ue037", "Run in terminal", false);
        CustomButton submitButton =
                new CustomButton("\ue86c", "Submit your result", false, ThemeColor.ACCENT_DARK.toString());
        runButton.setOnMouseClicked(e -> terminal.update(editor.getText()));
        submitButton.setOnMouseClicked(e -> openValidationPopup());

        bar.getChildren().addAll(controlsBox, textBox, terminal, runButton, submitButton);
        bar.setMinWidth(SIDEBAR_WIDTH);
        bar.setStyle("-fx-background-color: " + ThemeColor.EDITOR_SIDEBAR);
        return bar;
    }

    /**
     * Creates a new TextBox that is used for task description and name
     *
     * @return the scrollpane of the TextBox
     */
    private ScrollPane createTextBox() {
        Text title = new Text(exercise.getName());
        title.setWrappingWidth(SIDEBAR_WIDTH - BOX_PADDINGS * 2);
        title.getStyleClass().addAll("text", "heading", "small");

        Text description = new Text(exercise.getDescription());
        description.getStyleClass().addAll("text");
        description.setWrappingWidth(SIDEBAR_WIDTH - BOX_PADDINGS * 2);

        VBox textBox = new VBox(title, description);
        textBox.setPadding(new Insets(BOX_PADDINGS));
        textBox.setSpacing(10);

        ScrollPane pane = new ScrollPane();
        pane.setStyle("-fx-background-color: transparent");
        pane.setMinHeight(300);
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setContent(textBox);

        return pane;
    }

    /**
     * Saves the current exercise
     *
     * @param completion if the current solution is right or not
     * @return the successfulness of the operation
     */
    private boolean save(boolean completion) {
        if (exercise instanceof Level) {
            Level l = (Level) exercise;
            return LevelHandler.writeLevelData(l.getGroupId(), l.getId(), completion, editor.getText());
        } else if (exercise instanceof Challenge) {
            Challenge c = (Challenge) exercise;
            return ChallengeHandler.saveChallenge(c, editor.getText(), completion);
        }
        return false;
    }


    /**
     * Opens a Popup with the evaluation results and further controls
     */
    private void openValidationPopup() {
        popup.open();
        List<ValidationResult> results = Validation.run(editor.getText(), exercise.getTests());
        boolean success = ValidationResult.evaluate(results);
        if (save(success)) {
            VBox items = createValidationItems(results);
            Text heading = new Text(success ? "Congratulations!\nYou passed all the tests" : "Ooops! Try again.");
            heading.getStyleClass().addAll("text", "heading", "medium");
            heading.setTextAlignment(TextAlignment.CENTER);

            Text testsHeading = new Text("Tests (" + results.size() + ")");
            items.getChildren().add(0, testsHeading);
            testsHeading.getStyleClass().addAll("text", "heading", "small");
            HBox controls = createControlButtons(success);
            VBox popupContent = new VBox();
            popupContent.getChildren().addAll(heading, controls, items);
            popupContent.setSpacing(35);
            popupContent.setPadding(new Insets(40));
            popupContent.setAlignment(Pos.CENTER);
            popupContent.setPrefWidth(Popup.POPUP_WIDTH);
            popup.updateContent(popupContent);
        }
    }

    /**
     * Creates a list of ValidationResults
     *
     * @param results the given results
     * @return a vbox containing the results
     */
    private VBox createValidationItems(List<ValidationResult> results) {
        VBox items = new VBox();
        items.setSpacing(20);
        results.stream().map(ValidationItem::new).forEach(v -> items.getChildren().add(v));
        return items;
    }

    /**
     * Creates the control buttons for the Validation Popup
     *
     * @param success if the validation was successful (passed or failed)
     * @return an Hbox containing the buttons
     */
    private HBox createControlButtons(boolean success) {
        HBox buttons = new HBox();
        CustomButton mainButton = null;
        if (success && exercise instanceof Level) {
            Level l = (Level) exercise;
            Level nextLevel = LevelHandler.getNextLevel(l);
            if (nextLevel != null) {
                mainButton = new CustomButton("\ue16a", "Next Level", true);
                mainButton.setOnMouseClicked(e -> View.updateView(this, new ExerciseView(nextLevel)));
            }
        } else if (!success) {
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

    /**
     * Generates the help button displayed in the top right corner
     *
     * @return an HBox with the Help Button
     */
    private HBox getHelpButton() {
        Icon icon = new Icon("\ue887");
        Text info = new Text("Help");
        info.getStyleClass().add("text");
        HBox btn = new HBox(icon, info);
        btn.setSpacing(10);
        btn.setOnMouseClicked(this::openHelpPopup);
        Interaction.setClickable(btn, false);
        return btn;
    }

    /**
     * Opens the help button
     *
     * @param mouseEvent the click event that fired this operation
     */
    private void openHelpPopup(MouseEvent mouseEvent) {
        if (exercise instanceof Level) {
            Level l = (Level) exercise;
            Text heading = new Text("Help");
            heading.getStyleClass().addAll("text", "heading", "medium");
            VBox popupContent = new VBox(heading);
            popupContent.setAlignment(Pos.CENTER_LEFT);
            popupContent.setSpacing(20);
            popup.updateContent(popupContent);
            popup.open();
            if (l.getHelpText() != null) {
                Text helpText = new Text(l.getHelpText());
                helpText.getStyleClass().add("text");
                helpText.setWrappingWidth(Popup.POPUP_WIDTH);
                popupContent.getChildren().add(helpText);
            }
            if (l.getHelpCode() != null) {
                CodeEditor editor = new CodeEditor(l.getHelpCode());
                editor.setEditable(false);
                editor.setMaxHeight(150);
                editor.setAutoHeight(true);
                editor.setMaxWidth(Popup.POPUP_WIDTH);
                popupContent.getChildren().add(editor);
            }
            CustomButton closeBtn = new CustomButton("\ue5cd", "Close", true);
            closeBtn.setOnMouseClicked(e -> popup.close());
            popupContent.getChildren().add(closeBtn);
        }
    }

    /**
     * Returns back to overview based on the exercises Class
     */
    private void backToOverview() {
        if (exercise instanceof Level) {
            View.updateView(this, new MenuView(new CoursePage()));
        } else if (exercise instanceof Challenge) {
            View.updateView(this, new MenuView(new ChallengePage()));
        }
    }
}
