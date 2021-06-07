package daro.game.views;

import daro.game.io.UserData;
import daro.game.pages.PlaygroundPage;
import daro.game.ui.CodeEditor;
import daro.game.ui.CustomButton;
import daro.game.ui.Terminal;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;

public class EditorView extends View {

    private final static double TERMINAL_WIDTH = 360;

    /**
     * A view showing a CodeEditor and a Terminal used for playgrounds
     *
     * @param file playground file
     */
    public EditorView(File file) {
        try {
            String code = UserData.getPlayground(file);
            double buttonHeight = 50;
            VBox sidebar = new VBox();
            Terminal terminal = new Terminal(TERMINAL_WIDTH);
            CodeEditor editor = new CodeEditor(code);

            CustomButton runButton = new CustomButton("\ue037", "Run the program", buttonHeight, false);
            CustomButton closeButton = new CustomButton("\ue9ba", "Save & Close", buttonHeight, false, "#cc2610");
            runButton.setOnMouseClicked(e -> terminal.update(editor.getText()));
            closeButton.setOnMouseClicked(e -> {
                UserData.savePlayground(file, editor.getText());
                returnToOverview();
            });

            sidebar.getChildren().addAll(terminal, runButton, closeButton);
            this.getChildren().addAll(editor, sidebar);
        } catch (IOException e) {
            returnToOverview();
        }
    }

    private void returnToOverview() {
        this.getScene().setRoot(new MenuView(new PlaygroundPage()));
    }
}
