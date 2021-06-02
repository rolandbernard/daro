package daro.game.views;

import daro.game.main.Game;
import daro.game.main.Level;
import daro.game.main.UserData;
import daro.game.ui.CodeEditor;
import daro.game.ui.Terminal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LevelView extends View {

    private Level level;
    private final double SIDEBAR_WIDTH = 340;
    private final double BOX_PADDINGS = 30;
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
        this.getChildren().addAll(getLeftBar(), editor, getRightBar());
    }

    private VBox getLeftBar() {
        VBox bar = new VBox();
        double textBoxHeight = 300;
        double buttonHeight = 40;

        Text title = new Text(level.getName());
        title.setWrappingWidth(SIDEBAR_WIDTH - BOX_PADDINGS * 2);
        title.getStyleClass().addAll("text", "heading", "small");

        Text description = new Text(level.getDescription());
        description.getStyleClass().addAll("text");
        description.setWrappingWidth(SIDEBAR_WIDTH - BOX_PADDINGS * 2);

        VBox textBox = new VBox(title, description);
        textBox.setPadding(new Insets(BOX_PADDINGS));
        textBox.setSpacing(10);
        textBox.setPrefHeight(textBoxHeight);

        Terminal terminal = new Terminal(SIDEBAR_WIDTH, Game.HEIGHT - textBoxHeight - buttonHeight);

        HBox runButton = getRunButton(buttonHeight);
        runButton.setOnMouseClicked(e -> {
            terminal.update(editor.getText());
            UserData.writeLevelData(parentId, level.getId(), false, editor.getText());
        });

        bar.getChildren().addAll(textBox, terminal, runButton);
        bar.setMinWidth(SIDEBAR_WIDTH);
        bar.setPrefHeight(Game.HEIGHT);
        bar.setStyle("-fx-background-color: #1A0A47");
        return bar;
    }

    private VBox getRightBar() {
        VBox bar = new VBox();
        return bar;
    }

    private HBox getRunButton(double height) {
        HBox runButton = new HBox();
        runButton.setAlignment(Pos.CENTER);
        runButton.setPrefHeight(height);
        runButton.setStyle("-fx-background-color: #FF3D23");
        Text icon = new Text("\ue037");
        icon.getStyleClass().add("icon");
        Text text = new Text("Run code");
        text.getStyleClass().add("text");
        runButton.setCursor(Cursor.HAND);
        runButton.getChildren().addAll(icon, text);
        return runButton;
    }
}
