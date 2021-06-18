package daro.game.views;

import daro.game.io.PlaygroundHandler;
import daro.game.main.ThemeColor;
import daro.game.pages.PlaygroundPage;
import daro.game.ui.CodeEditor;
import daro.game.ui.CustomButton;
import daro.game.ui.Terminal;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;

/**
 * <strong>UI: <em>View</em></strong><br>
 * A view showing a CodeEditor and a Terminal used for playgrounds
 *
 * @author Daniel Planötscher
 */
public class EditorView extends View {
    /**
     * Generates a basic Editor View
     *
     * @param file playground file to display
     */
    public EditorView(File file) {
        try {
            String code = PlaygroundHandler.getPlayground(file);
            VBox sidebar = new VBox();
            Terminal terminal = new Terminal(360);
            CodeEditor editor = new CodeEditor(code);

            CustomButton runButton = new CustomButton("\ue037", "Run the program", false);
            CustomButton closeButton =
                    new CustomButton("\ue9ba", "Save & Close", false, ThemeColor.ACCENT_DARK.toString());
            runButton.setOnMouseClicked(e -> terminal.update(editor.getText()));
            closeButton.setOnMouseClicked(e -> {
                if (PlaygroundHandler.savePlayground(file, editor.getText())) {
                    returnToOverview();
                }
            });

            sidebar.getChildren().addAll(terminal, runButton, closeButton);
            this.getChildren().addAll(editor, sidebar);
        } catch (IOException e) {
            returnToOverview();
        }
    }

    /**
     * Sets the view to MenuView
     */
    private void returnToOverview() {
        View.updateView(this, new MenuView(new PlaygroundPage()));
    }
}
