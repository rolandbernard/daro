package daro.game.ui;

import daro.game.main.Game;
import daro.lang.interpreter.Interpreter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Terminal extends ScrollPane {
    private TextFlow textContent;
    private StringBuffer currentString;
    private PrintStream stream;
    public static final double STANDARD_WIDTH = 360;

    /**
     * A simple terminal that shows prints of code
     */
    public Terminal() {
        this.setHeight(Game.HEIGHT);
        this.setPrefWidth(STANDARD_WIDTH);
        init();
    }

    /**
     * A simple terminal that shows prints of code
     *
     * @param width  width of the Terminal
     * @param height height of the Terminal
     */
    public Terminal(double width, double height) {
        this.setPrefHeight(height);
        this.setPrefWidth(width);
        init();
    }

    private void init() {

        this.textContent = new TextFlow();

        VBox container = new VBox(textContent);
        container.setPrefHeight(this.getPrefHeight());
        container.setPadding(new Insets(20));

        this.setContent(container);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.getStyleClass().add("terminal");
        this.setStyle("-fx-background-color: #1D1F26;");

        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                currentString.append(new String(new byte[]{(byte) b}, StandardCharsets.UTF_8));
            }
        };
        stream = new PrintStream(out);
    }

    /**
     * Updates the content of the Terminal when a program is run
     *
     * @param code code that the terminal should run
     */
    public void update(String code) {
        Text status = new Text();
        this.currentString = new StringBuffer("\n\n\n");
        status.setWrappingWidth(this.getPrefWidth() - 40);
        try {
            Interpreter interpreter = new Interpreter(stream);
            interpreter.execute(code);
            status.setText("\n\nProgram terminated.");
            status.getStyleClass().addAll( "monospace");
        } catch(Exception e) {
            status.setText("\n\nProgram terminated with errors:\n" + e.getMessage());
            status.getStyleClass().addAll( "monospace", "terminal-error");
        }
        Text text = new Text(currentString.toString());
        text.getStyleClass().addAll( "monospace");
        text.setWrappingWidth(this.getPrefWidth() - 40);
        currentString = new StringBuffer();
        textContent.getChildren().addAll(text, status);
    }
}
