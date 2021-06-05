package daro.ide.debug;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * This class can be used to display text. It can be used to display normal
 * string, error messages and info messages. Using getPrintStream it is also
 * possible to get a {@link PrintStream} that will output directly onto the
 * terminal.
 * 
 * @author Roland Bernard
 */
public class Terminal extends ScrollPane {
    private TextFlow textFlow;

    /**
     * Create a new {@link Terminal}.
     */
    public Terminal() {
        getStyleClass().add("terminal");
        textFlow = new TextFlow();
        setContent(textFlow);
        textFlow.heightProperty().addListener((observable, oldValue, newValue) -> {
            setVvalue(1.0);
        });
    }

    /**
     * Append a {@link Text} object to the terminals output. This method may be
     * called from a non-Javafx thread.
     *
     * @param text The text object that should be added
     */
    synchronized private void appendText(Text text) {
        Platform.runLater(() -> {
            textFlow.getChildren().add(text);
        });
    }

    /**
     * Print the given string using default style.
     *
     * @param content The string that should be printed
     */
    public void printString(String content) {
        Text text = new Text(content);
        text.getStyleClass().add("monospace");
        appendText(text);
    }

    /**
     * Print the given string using the error style.
     *
     * @param content The string that should be printed
     */
    public void printError(String content) {
        Text text = new Text(content);
        text.getStyleClass().addAll("monospace", "terminal-error");
        appendText(text);
    }

    /**
     * Print the given string using the info style.
     *
     * @param content The string that should be printed
     */
    public void printInfo(String content) {
        Text text = new Text(content);
        text.getStyleClass().addAll("monospace", "terminal-info");
        appendText(text);
    }

    /**
     * Clear the output of the terminal.
     */
    public void clear() {
        textFlow.getChildren().clear();
    }

    /**
     * Create and return a {@link PrintStream} that outputs onto the terminal using
     * the default style.
     *
     * @return The created {@link PrintStream}
     */
    public PrintStream getPrintStream() {
        return new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                printString(String.valueOf((char)b));
            }
        });
    }
}
