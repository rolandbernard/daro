package daro.ide.main;

import java.nio.file.Path;

import daro.ide.debug.Terminal;
import daro.ide.editor.EditorTabs;
import daro.lang.interpreter.Interpreter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ExecutionPalette extends VBox {
    private Thread thread;
    private Button run;
    private Button debug;
    private Button stop;
    private Button next;
    private Button stepOver;
    private Button stepInto;
    private Button stepOut;
    private Button restart;
    private Button clear;

    public ExecutionPalette(EditorTabs editor, Terminal terminal, Interpreter interpreter) {
        thread = null;
        run = buildIconButton("\ue037", event -> {
            startRunning();
            editor.saveOpenIfNamed();
            Path file = editor.getOpenFile();
            String content = editor.getOpenContent();
            thread = new Thread(() -> {
            interpreter.reset();
            System.out.println("Test");
                try {
                    if (file != null) {
                        interpreter.execute(file);
                    } else {
                        interpreter.execute(content);
                    }
                } catch (Exception error) {
                    terminal.printError(error.toString() + "\n");
                }
                stopRunning();
            });
            thread.run();
        });
        debug = buildIconButton("\ue868", event -> {});
        stop = buildIconButton("\ue047", event -> {
            killThread();
            startRunning();
        });
        next = buildIconButton("\uf1df", event -> {});
        stepOver = buildIconButton("\ue15a", event -> {});
        stepInto = buildIconButton("\uea77", event -> {});
        stepOut = buildIconButton("\ue9ba", event -> {});
        restart = buildIconButton("\ue042", event -> {});
        clear = buildIconButton("\ue872", event -> {
            terminal.clear();
            interpreter.reset();
        });

        stop.setDisable(true);
        next.setDisable(true);
        stepOver.setDisable(true);
        stepInto.setDisable(true);
        stepOut.setDisable(true);
        restart.setDisable(true);

        Region spacerA = new Region();
        VBox.setVgrow(spacerA, Priority.ALWAYS);
        Region spacerB = new Region();
        VBox.setVgrow(spacerB, Priority.ALWAYS);

        setSpacing(5);
        setAlignment(Pos.CENTER);
        getStyleClass().add("execution-palette");
        getChildren().addAll(run, debug, stop, spacerA, next, stepOver, stepInto, stepOut, restart, spacerB, clear);
    }

    private void startRunning() {
        run.setDisable(true);
        debug.setDisable(true);
        stop.setDisable(false);
    }

    private void stopRunning() {
        run.setDisable(false);
        debug.setDisable(false);
        stop.setDisable(true);
    }

    @SuppressWarnings("deprecation")
    private void killThread() {
        thread.stop();
    }

    private Button buildIconButton(String iconString, EventHandler<ActionEvent> onClick) {
        Text icon = new Text(iconString);
        icon.getStyleClass().add("icon");
        Button button = new Button();
        button.setGraphic(icon);
        button.setOnAction(onClick);
        return button;
    }
}
