package daro.ide.main;

import java.nio.file.Path;

import daro.ide.debug.Terminal;
import daro.ide.debug.ScopeViewer;
import daro.ide.editor.EditorTabs;
import daro.lang.interpreter.Interpreter;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class Workspace extends SplitPane {
    private EditorTabs editor;
    private Terminal terminal;
    private ScopeViewer scope;
    private Interpreter interpreter;
    private ExecutionPalette palette;

    public Workspace() {
        editor = new EditorTabs();
        terminal = new Terminal();
        interpreter = new Interpreter(terminal.getPrintStream());
        scope = new ScopeViewer(interpreter.getContext().getScope());
        palette = new ExecutionPalette(editor, terminal, scope, interpreter);

        Region divider = new Region();
        divider.getStyleClass().add("pane-divider");
        HBox.setHgrow(editor, Priority.ALWAYS);
        HBox top = new HBox(editor, divider, palette);

        TabPane console = new TabPane();
        Tab terminalTab = new Tab("Terminal", terminal);
        terminalTab.setClosable(false);
        Tab scopeTab = new Tab("Variables", scope);
        scopeTab.setClosable(false);
        console.getTabs().addAll(terminalTab, scopeTab);
        console.setMaxHeight(400);

        getItems().addAll(top, console);
        setOrientation(Orientation.VERTICAL);
        terminal.printInfo("IDE initialized.\n");
    }

    public boolean allowClosing() {
        return editor.allowClosing();
    }

    public void openNewFile(Path file) {
        editor.openFile(file);
    }
}
