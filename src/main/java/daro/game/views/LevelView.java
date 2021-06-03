package daro.game.views;

import daro.game.main.Game;
import daro.game.main.Level;
import daro.game.main.UserData;
import daro.game.pages.CoursePage;
import daro.game.ui.CodeEditor;
import daro.game.ui.CustomButton;
import daro.game.ui.Terminal;
import daro.game.ui.ValidationItem;
import daro.game.validation.Validation;
import daro.game.validation.ValidationResult;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
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
    private static final double BOX_PADDINGS = 30;
    private static final double POPUP_HEIGHT = 300;

    private Level level;
    private CodeEditor editor;
    private long parentId;
    private StackPane popup;

    /**
     * <strong>UI: <em>View</em></strong><br>
     * A view to display and solve levels.
     *
     * @param level the level shown in the view
     */
    public LevelView(long parentId, Level level) {
        this.level = level;
        this.parentId = parentId;
        editor = new CodeEditor(level.getCode());
        HBox mainContent = new HBox(getSidebar(), editor);

        this.popup = createPopup();
        StackPane wholeContent = new StackPane();
        wholeContent.getChildren().addAll(mainContent, popup);
        wholeContent.setAlignment(Pos.CENTER);
        this.getChildren().add(wholeContent);
    }

    private VBox getSidebar() {
        VBox bar = new VBox();
        double textBoxHeight = 250;
        double buttonHeight = 40;
        double backBtnHeight = 30;
        double terminalHeight = Game.HEIGHT - textBoxHeight - buttonHeight * 2 - backBtnHeight;

        ScrollPane textBox = createTextBox(textBoxHeight);
        Terminal terminal = new Terminal(SIDEBAR_WIDTH, terminalHeight);
        HBox backButton = createBackButton(backBtnHeight, SIDEBAR_WIDTH);

        CustomButton runButton = new CustomButton("\ue037", "Run the program", SIDEBAR_WIDTH, buttonHeight, false);
        CustomButton submitButton = new CustomButton("\ue86c", "Submit your result", SIDEBAR_WIDTH, buttonHeight, true);
        runButton.setOnMouseClicked(e -> terminal.update(editor.getText()));
        submitButton.setOnMouseClicked(this::openValidationPopup);

        bar.getChildren().addAll(backButton, textBox, terminal, runButton, submitButton);
        bar.setMinWidth(SIDEBAR_WIDTH);
        bar.setPrefHeight(Game.HEIGHT);
        bar.setStyle("-fx-background-color: #2b2e3a");
        return bar;
    }

    private StackPane createPopup() {
        StackPane popup = new StackPane();
        popup.setStyle("-fx-background-color: rgba(0,0,0,0.5)");
        popup.setVisible(false);
        return popup;
    }

    private ScrollPane createTextBox(double textBoxHeight) {
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
        pane.setPrefHeight(textBoxHeight);
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setContent(textBox);

        return pane;
    }

    private HBox createBackButton(double height, double width) {
        Text icon = new Text("\ue5c4");
        icon.getStyleClass().add("icon");

        Text label = new Text("Back to overview");
        label.getStyleClass().add("text");

        HBox btn = new HBox();
        btn.setSpacing(10);
        btn.getChildren().addAll(icon, label);
        btn.setPrefHeight(height);
        btn.setPrefWidth(width);
        btn.setOnMouseClicked(e -> {
            save(ValidationResult.evaluate(Validation.run(editor.getText(), level.getTests())));
            this.getScene().setRoot(new MenuView(new CoursePage()));
        });
        btn.setCursor(Cursor.HAND);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(10));
        return btn;
    }


    private boolean save(boolean completion) {
        return UserData.writeLevelData(parentId, level.getId(), completion, editor.getText());
    }

    private void openValidationPopup(MouseEvent mouseEvent) {
        popup.setVisible(true);
        ScrollPane innerPopup = new ScrollPane();
        VBox popupContent = new VBox();
        innerPopup.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        innerPopup.setMaxHeight(400);
        innerPopup.setMaxWidth(640);
        innerPopup.setContent(popupContent);
        innerPopup.setStyle("-fx-background-color: #2b2e3a; -fx-background-radius: 25px;");
        List<ValidationResult> results = Validation.run(editor.getText(), level.getTests());
        boolean success = ValidationResult.evaluate(results);
        save(success);

        VBox items = createValidationItems(results);
        Text heading = new Text(success ? "Congratulations!\nYou passed all the tests" : "Ooops! Try again.");
        heading.getStyleClass().addAll("text", "heading", "small");
        heading.setTextAlignment(TextAlignment.CENTER);

        HBox controls = createControlButtons(success);
        popupContent.getChildren().addAll(heading, items, controls);
        popupContent.setSpacing(30);
        popupContent.setPadding(new Insets(40));
        popupContent.setAlignment(Pos.CENTER);
        popupContent.setFillWidth(true);
        popup.getChildren().removeAll();
        popup.getChildren().add(innerPopup);
    }

    private VBox createValidationItems(List<ValidationResult> results) {
        VBox items = new VBox();
        items.setSpacing(20);
        items.setAlignment(Pos.CENTER);
        results.stream().map(ValidationItem::new).forEach(v -> items.getChildren().add(v));
        return items;
    }

    private HBox createControlButtons(boolean success) {
        double buttonHeight = 40;
        double buttonWidth = 180;
        HBox buttons = new HBox();
        CustomButton mainButton = null;
        if (success) {
            mainButton = new CustomButton("\ue16a", "Next Level", buttonWidth, buttonHeight, false);
        } else {
            mainButton = new CustomButton("\ue5d5", "Try again", buttonWidth, buttonHeight, false);
            mainButton.setOnMouseClicked(e -> popup.setVisible(false));
        }
        CustomButton backButton = new CustomButton("\ue5c4", "Back to overview", buttonWidth, buttonHeight, true);
        backButton.setOnMouseClicked(e -> backToOverview());
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(20);
        buttons.getChildren().add(backButton);
        buttons.getChildren().add(mainButton);
        return buttons;
    }

    private void backToOverview() {
        this.getScene().setRoot(new MenuView(new CoursePage()));
    }


}
