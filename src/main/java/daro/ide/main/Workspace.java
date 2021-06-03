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

public class Workspace extends SplitPane {
    private EditorTabs editor;
    private Terminal terminal;
    private ScopeViewer scope;
    private Interpreter interpreter;

    public Workspace() {
        editor = new EditorTabs();
        terminal = new Terminal();
        interpreter = new Interpreter(terminal.getPrintStream());
        scope = new ScopeViewer(interpreter.getContext().getScope());
        setOrientation(Orientation.VERTICAL);
        TabPane console = new TabPane();
        console.setPrefHeight(300);
        console.setMaxHeight(450);
        Tab terminalTab = new Tab("Terminal", terminal);
        terminalTab.setClosable(false);
        Tab scopeTab = new Tab("Variables", scope);
        scopeTab.setClosable(false);
        console.getTabs().addAll(terminalTab, scopeTab);
        getItems().addAll(editor, console);
        terminal.printInfo("IDE initialized.");
    }

    public boolean allowClosing() {
        return editor.allowClosing();
    }

    public void openNewFile(Path file) {
        editor.openFile(file);
    }
}
