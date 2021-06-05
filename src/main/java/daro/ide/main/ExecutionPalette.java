package daro.ide.main;

import java.nio.file.Path;

import daro.ide.debug.Debugger;
import daro.ide.debug.Interrupter;
import daro.ide.debug.Terminal;
import daro.ide.debug.ScopeViewer;
import daro.ide.editor.EditorTabs;
import daro.lang.ast.Position;
import daro.lang.interpreter.DaroException;
import daro.lang.interpreter.ExecutionObserver;
import daro.lang.interpreter.Interpreter;
import daro.lang.interpreter.Scope;
import javafx.application.Platform;
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
    private Button clear;

    public ExecutionPalette(EditorTabs editor, Terminal terminal, ScopeViewer scope, Interpreter interpreter) {
        this.editor = editor;
        this.terminal = terminal;
        this.interpreter = interpreter;
        this.scope = scope;
        this.debugger = new Debugger(this);

        run = buildIconButton("\ue037", event -> executeCode(false));
        debug = buildIconButton("\ue868", event -> executeCode(true));
        stop = buildIconButton("\ue047", event -> {
            thread.interrupt();
            if (debugger != null) {
                debugger.next();
            }
        });
        next = buildIconButton("\uf1df", event -> {
            debugger.next();
        });
        stepOver = buildIconButton("\ue15a", event -> {
            debugger.stepOver();
        });
        stepInto = buildIconButton("\uea77", event -> {
            debugger.stepInto();
        });
        stepOut = buildIconButton("\ue9ba", event -> {
            debugger.stepOut();
        });
        clear = buildIconButton("\ue872", event -> {
            terminal.clear();
            interpreter.reset();
            scope.reload();
            editor.resetHighlighting();
        });

        stop.setDisable(true);
        stopDebugging();

        Region spacerA = new Region();
        VBox.setVgrow(spacerA, Priority.ALWAYS);
        Region spacerB = new Region();
        VBox.setVgrow(spacerB, Priority.ALWAYS);

        setSpacing(5);
        setAlignment(Pos.CENTER);
        getStyleClass().add("execution-palette");
        getChildren().addAll(run, debug, stop, spacerA, next, stepOver, stepInto, stepOut, spacerB, clear);
    }

    private void executeCode(boolean withDebugger) {
        run.setDisable(true);
        debug.setDisable(true);
        stop.setDisable(false);
        editor.saveOpenIfNamed();
        editor.resetHighlighting();
        Path file = editor.getOpenFile();
        String content = editor.getOpenContent();
        interpreter.reset();
        debugger.setBreakpoints(editor.getBreakpoints());
        debugger.next();
        thread = new Thread(() -> {
            try {
                ExecutionObserver[] observers;
                if (withDebugger) {
                    observers = new ExecutionObserver[] {
                        debugger, new Interrupter()
                    };
                } else {
                    observers = new ExecutionObserver[] {
                        new Interrupter()
                    };
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
            stopRunning();
            stopDebugging();
        });
        thread.start();
    }

    private void stopRunning() {
        Platform.runLater(() -> {
            run.setDisable(false);
            debug.setDisable(false);
            stop.setDisable(true);
            terminal.printInfo("Program terminated.\n");
            scope.setScope(interpreter.getContext().getScope());
            scope.reload();
        });
    }

    public void startDebugging(Scope debugScope, Position location) {
        Platform.runLater(() -> {
            next.setDisable(false);
            stepOver.setDisable(false);
            stepInto.setDisable(false);
            stepOut.setDisable(false);
            editor.highlightDebug(location);
            scope.setScope(debugScope);
            scope.reload();
        });
    }

    public void stopDebugging() {
        debugger.setBreakpoints(editor.getBreakpoints());
        Platform.runLater(() -> {
            editor.resetHighlighting();
            next.setDisable(true);
            stepOver.setDisable(true);
            stepInto.setDisable(true);
            stepOut.setDisable(true);
        });
    }

    private Button buildIconButton(String iconString, EventHandler<ActionEvent> onClick) {
        Text icon = new Text(iconString);
        icon.getStyleClass().add("icon");
        Button button = new Button();
        button.setGraphic(icon);
        button.setOnAction(onClick);
        return button;
    }

    public void allowClosing() {
        if (thread != null) {
            thread.interrupt();
        }
    }
}
