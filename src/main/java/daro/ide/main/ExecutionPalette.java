package daro.ide.main;

import java.nio.file.Path;
import java.util.Set;

import daro.ide.debug.Debugger;
import daro.ide.debug.Interrupter;
import daro.ide.debug.Terminal;
import daro.ide.debug.ScopeViewer;
import daro.ide.editor.EditorTabs;
import daro.lang.interpreter.DaroException;
import daro.lang.interpreter.ExecutionObserver;
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
    private EditorTabs editor;
    private Terminal terminal;
    private ScopeViewer scope;
    private Interpreter interpreter;

    private Thread thread;
    private Debugger debugger;
    private Button run;
    private Button debug;
    private Button stop;
    private Button next;
    private Button stepOver;
    private Button stepInto;
    private Button stepOut;
    private Button restart;
    private Button clear;

    public ExecutionPalette(EditorTabs editor, Terminal terminal, ScopeViewer scope, Interpreter interpreter) {
        this.editor = editor;
        this.terminal = terminal;
        this.interpreter = interpreter;
        this.scope = scope;

        run = buildIconButton("\ue037", event -> executeCode(false));
        debug = buildIconButton("\ue868", event -> executeCode(true));
        stop = buildIconButton("\ue047", event -> {
            thread.interrupt();
        });
        next = buildIconButton("\uf1df", event -> {});
        stepOver = buildIconButton("\ue15a", event -> {});
        stepInto = buildIconButton("\uea77", event -> {});
        stepOut = buildIconButton("\ue9ba", event -> {});
        restart = buildIconButton("\ue042", event -> {});
        clear = buildIconButton("\ue872", event -> {
            terminal.clear();
            interpreter.reset();
            scope.reload();
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

    private void executeCode(boolean withDebugger) {
        run.setDisable(true);
        debug.setDisable(true);
        stop.setDisable(false);
        editor.saveOpenIfNamed();
        Path file = editor.getOpenFile();
        String content = editor.getOpenContent();
        thread = new Thread(() -> {
            interpreter.reset();
            try {
                ExecutionObserver[] observers;
                if (withDebugger) {
                    debugger = new Debugger(Set.of());
                    observers = new ExecutionObserver[] { new Interrupter(), debugger };
                } else {
                    observers = new ExecutionObserver[] { new Interrupter() };
                }
                if (file != null) {
                    interpreter.execute(file, observers);
                } else {
                    interpreter.execute(content, observers);
                }
            } catch (DaroException error) {
                editor.highlightError(error);
                terminal.printError(error.toString() + "\n");
            } catch (Exception error) {
                terminal.printError(error.toString() + "\n");
            }
            run.setDisable(false);
            debug.setDisable(false);
            stop.setDisable(true);
            scope.setScope(interpreter.getContext().getScope());
            scope.reload();
        });
        thread.start();
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
