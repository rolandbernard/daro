package daro.game.ui;

import daro.game.main.Game;
import daro.lang.interpreter.Interpreter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Terminal extends VBox {
    private Text textContent;
    private StringBuffer textString;
    private PrintStream stream;

    /**
     * A simple terminal that shows prints of code
     */
    public Terminal() {
        this.setPrefHeight(Game.HEIGHT);
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
        this.setPrefHeight(height);
        this.setPrefWidth(width);
        init();
    }

    private void init() {
        this.setStyle("-fx-background-color: #1D1F26");
        this.textContent = new Text();
        this.textString = new StringBuffer();
        this.textContent.setWrappingWidth(this.getPrefWidth() - 40);
        this.textContent.getStyleClass().add("text");
        this.setAlignment(Pos.BOTTOM_LEFT);
        this.setPadding(new Insets(20));
        this.getChildren().add(textContent);

        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                textString.append(new String(new byte[]{(byte) b}, StandardCharsets.UTF_8));
                textContent.setText(textString.toString());
            }
        };
        stream = new PrintStream(out);
    }

    /**
     * Updates the content of the Terminal when the code prints something
     *
     * @param code code that the terminal should run
     */
    public void update(String code) {
        Interpreter interpreter = new Interpreter(stream);
        interpreter.execute(code);
    }
}
