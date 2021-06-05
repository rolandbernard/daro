package daro.ide.debug;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Terminal extends ScrollPane {
    private TextFlow textFlow;

    public Terminal() {
        getStyleClass().add("terminal");
        textFlow = new TextFlow();
        setContent(textFlow);
        textFlow.heightProperty().addListener((observable, oldValue, newValue) -> {
            setVvalue(1.0);
        });
    }

    /**
     * Append a {@link Text} object to the terminals output. This method may be called from a
     * non-Javafx thread.
     *
     * @param text The text object that should be added
     */
    synchronized private void appendText(Text text) {
        Platform.runLater(() -> {
            textFlow.getChildren().add(text);
        });
    }

    public void printString(String content) {
        Text text = new Text(content);
        text.getStyleClass().add("monospace");
        appendText(text);
    }

    public void printError(String content) {
        Text text = new Text(content);
        text.getStyleClass().addAll("monospace", "terminal-error");
        appendText(text);
    }

    public void printInfo(String content) {
        Text text = new Text(content);
        text.getStyleClass().addAll("monospace", "terminal-info");
        appendText(text);
    }

    public void clear() {
        textFlow.getChildren().clear();
    }

    public PrintStream getPrintStream() {
        return new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                printString(String.valueOf((char)b));
            }
        });
    }
}
