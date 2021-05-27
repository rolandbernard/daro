package daro.ide.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import daro.game.ui.Terminal;
import daro.ide.debug.ScopeViewer;
import daro.ide.editor.EditorTabs;
import daro.lang.interpreter.Interpreter;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class Workspace extends SplitPane {
    private EditorTabs editor;
    private Terminal terminal;
    private ScopeViewer scope;
    private Interpreter interpreter;

    public Workspace() {
        editor = new EditorTabs();
        interpreter = new Interpreter();
        terminal = new Terminal();
        scope = new ScopeViewer(interpreter.getGlobalScope());
        setOrientation(Orientation.VERTICAL);
        TabPane console = new TabPane();
        console.setMaxHeight(450);
        Tab terminalTab = new Tab("Terminal", terminal);
        terminalTab.setClosable(false);
        Tab scopeTab = new Tab("Variables", scope);
        scopeTab.setClosable(false);
        console.getTabs().addAll(terminalTab, scopeTab);
        getItems().addAll(editor, console);
        sceneProperty().addListener((observable, old, scene) -> {
            KeyCombination keys = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
            scene.getAccelerators().put(keys, () -> {
                Path file = editor.getOpenFile();
                String content = editor.getOpenContent();
                try {
                    Files.writeString(file, content);
                } catch (IOException e) { }
            });
        });
    }

    public void openNewFile(Path file) {
        editor.openFile(file);
    }
}
