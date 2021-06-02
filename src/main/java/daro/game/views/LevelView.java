package daro.game.views;

import daro.game.main.Game;
import daro.game.main.Level;
import daro.game.main.UserData;
import daro.game.pages.CoursePage;
import daro.game.ui.CodeEditor;
import daro.game.ui.CustomButton;
import daro.game.ui.Terminal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class LevelView extends View {

    private final double SIDEBAR_WIDTH = 340;
    private final double BOX_PADDINGS = 30;

    private Level level;
    private CodeEditor editor;
    private long parentId;

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

        StackPane wholeContent = new StackPane();
        wholeContent.getChildren().addAll(mainContent, createPopup());
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

        bar.getChildren().addAll(backButton, textBox, terminal, runButton, submitButton);
        bar.setMinWidth(SIDEBAR_WIDTH);
        bar.setPrefHeight(Game.HEIGHT);
        bar.setStyle("-fx-background-color: #2b2e3a");
        return bar;
    }

    private StackPane createPopup() {
        StackPane popup = new StackPane();
        Text test = new Text("test");
        popup.getChildren().add(test);
        popup.setAlignment(Pos.CENTER);
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
            save(false);
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
}
