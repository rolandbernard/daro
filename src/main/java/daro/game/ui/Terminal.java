package daro.game.ui;

import daro.game.main.ThemeColor;
import daro.lang.interpreter.Interpreter;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;


/**
 * <strong>UI: <em>Component</em></strong><br>
 * A simple terminal that runs code and shows its printed values
 *
 * @author Daniel Planötscher
 */
public class Terminal extends VBox {
    private TextFlow textContent;
    private StringBuffer currentString;
    private PrintStream stream;
    private ScrollPane content;
    public static final double STANDARD_WIDTH = 360;

    /**
     * Generates a basic terminal
     */
    public Terminal() {
        setMinWidth(STANDARD_WIDTH);
        init();
    }

    /**
     * Generates a basic terminal with a standard width
     *
     * @param width width of the Terminal
     */
    public Terminal(double width) {
        setMinWidth(width);
        setPrefHeight(Integer.MAX_VALUE);
        init();
    }

    /**
     * Initializes the styling of a terminal
     */
    private void init() {
        Text title = new Text("Terminal");
        title.getStyleClass().addAll("heading", "tiny", "text", "monospace");
        textContent = new TextFlow();
        VBox container = new VBox(textContent);
        container.setPadding(new Insets(10, 0, 0, 0));
        content = new ScrollPane();
        content.setContent(container);
        content.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        content.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        content.setStyle("-fx-background-color: transparent;");
        setStyle("-fx-background-color: " + ThemeColor.TERMINAL_BACKGROUND);
        setPadding(new Insets(20));
        getChildren().addAll(title, content);

        textContent.heightProperty().addListener((observable, oldValue, newValue) -> {
            content.setVvalue(1.0);
        });

        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                currentString.append(new String(new byte[] {
                    (byte)b
                }, StandardCharsets.UTF_8));
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
        currentString = new StringBuffer("\n\n");
        status.setWrappingWidth(this.getPrefWidth() - 40);
        try {
            Interpreter interpreter = new Interpreter(stream);
            interpreter.execute(code);
            status.setText("\n\nProgram terminated.\n");
        } catch (Exception e) {
            status.setText("\n\nProgram terminated with errors:\n" + e.getMessage() + "\n");
            status.getStyleClass().add("terminal-error");
        }
        status.getStyleClass().add("monospace");
        Text text = new Text(currentString.toString());
        text.getStyleClass().addAll("monospace");
        text.setWrappingWidth(this.getPrefWidth() - 40);
        textContent.getChildren().addAll(text, status);
    }
}
