package daro.ide.main;

import java.nio.file.Path;
import java.util.Stack;

import daro.ide.debug.DebugController;
import daro.ide.debug.Debugger;
import daro.ide.debug.Interrupter;
import daro.ide.debug.Terminal;
import daro.ide.debug.ScopeViewer;
import daro.ide.debug.StackContext;
import daro.ide.editor.EditorTabs;
import daro.lang.interpreter.DaroException;
import daro.lang.interpreter.ExecutionObserver;
import daro.lang.interpreter.Interpreter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * This class implements a simple set of buttons for executing code. The palette
 * is linked to an {@link EditorTabs}, {@link Terminal}, {@link ScopeViewer} and
 * {@link Interpreter}.
 *
 * @author Roland Bernard
 */
public class ExecutionPalette extends VBox implements DebugController {
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
    private Button step;
    private Button stepOver;
    private Button stepInto;
    private Button stepOut;
    private Button clear;

    /**
     * Create a new {@link ExecutionPalette} linked to the given editor, terminal,
     * scope, and interpreter.
     *
     * @param editor      The editor to link the palette to
     * @param terminal    The terminal to link the palette to
     * @param scope       The scope viewer to link the palette to
     * @param interpreter The interpreter to link the palette to
     */
    public ExecutionPalette(EditorTabs editor, Terminal terminal, ScopeViewer scope, Interpreter interpreter) {
        this.editor = editor;
        this.terminal = terminal;
        this.interpreter = interpreter;
        this.scope = scope;
        this.debugger = new Debugger(this);

        run = buildIconButton("\ue037", event -> executeCode(false), "Run");
        debug = buildIconButton("\ue868", event -> executeCode(true), "Debug");
        stop = buildIconButton("\ue047", event -> {
            editor.resetHighlighting();
            thread.interrupt();
            if (debugger != null) {
                debugger.next();
            }
        }, "Stop");
        next = buildIconButton("\uf1df", event -> {
            editor.resetHighlighting();
            debugger.next();
        }, "Next");
        step = buildIconButton("\ue409", event -> {
            editor.resetHighlighting();
            debugger.step();
        }, "Step");
        stepOver = buildIconButton("\ue15a", event -> {
            editor.resetHighlighting();
            debugger.stepOver();
        }, "Step over");
        stepInto = buildIconButton("\uea77", event -> {
            editor.resetHighlighting();
            debugger.stepInto();
        }, "Step into");
        stepOut = buildIconButton("\ue9ba", event -> {
            editor.resetHighlighting();
            debugger.stepOut();
        }, "Step out");
        clear = buildIconButton("\ue872", event -> {
            editor.resetHighlighting();
            terminal.clear();
            interpreter.reset();
            scope.reload();
        }, "Clear");

        stop.setDisable(true);
        stopDebugging();

        Region spacerA = new Region();
        VBox.setVgrow(spacerA, Priority.ALWAYS);
        Region spacerB = new Region();
        VBox.setVgrow(spacerB, Priority.ALWAYS);

        setSpacing(5);
        setAlignment(Pos.CENTER);
        getStyleClass().add("execution-palette");
        getChildren().addAll(run, debug, stop, spacerA, next, step, stepOver, stepInto, stepOut, spacerB, clear);
    }

    /**
     * Build a new button with the given icon, tooltip and action.
     *
     * @param iconString The icon of the button
     * @param onClick    The handler to execute on action
     * @param tooltip    The tooltip for the button
     * @return The resulting button
     */
    private Button buildIconButton(String iconString, EventHandler<ActionEvent> onClick, String tooltip) {
        Text icon = new Text(iconString);
        icon.getStyleClass().add("icon");
        Button button = new Button();
        button.setGraphic(icon);
        button.setOnAction(onClick);
        button.setTooltip(new Tooltip(tooltip));
        return button;
    }

    /**
     * Start execution the code in the currently open editor.
     *
     * @param withDebugger Whether we should execute with the debugger or not
     */
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
        debugger.reset();
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
                terminal.printInfo("Program terminated.\n");
            } catch (DaroException error) {
                editor.highlightError(error);
                terminal.printError(error.toString() + "\n");
            } catch (Exception error) {
                terminal.printError(error.toString() + "\n");
            }
            stopDebugging();
            stopRunning();
        });
        thread.start();
    }

    /**
     * Executed after execution of the user code has stopped
     */
    private void stopRunning() {
        Platform.runLater(() -> {
            run.setDisable(false);
            debug.setDisable(false);
            stop.setDisable(true);
            scope.setScope(interpreter.getContext().getScope());
            scope.setStack(new Stack<>());
            scope.reload();
        });
    }

    @Override
    public void startDebugging(Stack<StackContext> context) {
        Platform.runLater(() -> {
            next.setDisable(false);
            step.setDisable(false);
            stepOver.setDisable(false);
            stepInto.setDisable(false);
            stepOut.setDisable(false);
            editor.highlightDebug(context.peek().getPosition(), context.peek().getLine());
            scope.setScope(context.peek().getScope());
            scope.setStack(context);
            scope.reload();
        });
    }

    @Override
    public void stopDebugging() {
        debugger.setBreakpoints(editor.getBreakpoints());
        Platform.runLater(() -> {
            next.setDisable(true);
            step.setDisable(true);
            stepOver.setDisable(true);
            stepInto.setDisable(true);
            stepOut.setDisable(true);
        });
    }

    /**
     * This method should be executed before allowing the user to close the window.
     * This will terminate the currently executed program.
     */
    public void allowClosing() {
        if (thread != null) {
            thread.interrupt();
            if (debugger != null) {
                debugger.next();
            }
        }
    }
}
