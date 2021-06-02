package daro.game.ui;

import daro.game.main.Game;
import daro.lang.interpreter.Interpreter;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Terminal extends ScrollPane {
    private Text textContent;
    private StringBuffer textString;
    private PrintStream stream;
    private SimpleDateFormat dateFormatter;

    /**
     * A simple terminal that shows prints of code
     */
    public Terminal() {
        this.setHeight(Game.HEIGHT);
        this.setPrefWidth(360);
        init();
    }

    /**
     * A simple terminal that shows prints of code
     *
     * @param width  width of the Terminal
     * @param height height of the Terminal
     */
    public Terminal(double width, double height) {
        this.setHeight(height);
        this.setPrefHeight(height);
        this.setPrefWidth(width);
        init();
    }

    private void init() {
        this.textString = new StringBuffer();

        this.textContent = new Text();
        this.textContent.setWrappingWidth(this.getPrefWidth() - 40);
        this.textContent.getStyleClass().addAll( "monospace");

        this.dateFormatter = new SimpleDateFormat("HH:mm");

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
                textString.append(new String(new byte[]{(byte) b}, StandardCharsets.UTF_8));
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
        try {
            Interpreter interpreter = new Interpreter(stream);
            textString.append("(");
            textString.append(dateFormatter.format(new Date()));
            textString.append("): ");
            interpreter.execute(code);
            textString.append("\n\nProgram terminated without errors.\n\n\n");
        } catch(Exception e) {
            textString.append("\n\nProgram terminated with errors.\n\n\n");
            textString.append(e.getMessage());
        }
        textContent.setText(textString.toString());
    }
}
