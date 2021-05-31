package daro.game.ui;

import daro.game.main.Game;
import daro.lang.interpreter.Interpreter;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Terminal extends VBox {
    private TextFlow textFlow;

    /**
     *
     */
    public Terminal() {
        this.setPrefHeight(Game.HEIGHT);
        this.setPrefWidth(360);
        init();
    }

    /**
     * 
     * @param width
     *            TODO TOFIX
     * @param height
     *            TODO TOFIX
     */
    public Terminal(double width, double height) {
        this.setPrefHeight(height);
        this.setPrefWidth(width);
        init();
    }

    private void init() {
        this.setStyle("-fx-background-color: #1D1F26");
        this.textFlow = new TextFlow();
        this.textFlow.getStyleClass().add("text");
        this.getChildren().add(textFlow);
    }

    public void update(String code) {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                byte[] ints = new byte[1];
                ints[0] = (byte) b;
                try {
                    Text text = new Text();
                    text.setText(new String(ints, "UTF-8"));
                    textFlow.getChildren().add(text);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        };
        PrintStream stream = new PrintStream(out);
        Interpreter interpreter = new Interpreter(stream);
        interpreter.execute(code);

    }
}
