package daro.ide.editor;

import java.nio.file.Path;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import daro.lang.ast.Position;
import daro.lang.interpreter.DaroException;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class EditorTabs extends TabPane {
    private Map<Path, EditorTab> tabs;
    private EditorContextMenu menu;

    public EditorTabs() {
        tabs = new HashMap<>();
        menu = new EditorContextMenu(this);
        setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
        Tab addTab = new Tab("+");
        addTab.setClosable(false);
        addTab.getStyleClass().add("add-tab");
        addTab.setOnSelectionChanged(event -> {
            addTab(new EditorTab(""));
        });
        getTabs().add(addTab);
        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            ((EditorTab)newValue).setEditorContextMenu(menu);
        });
        setContextMenu(menu);
    }

    private void addTab(EditorTab tab) {
        List<Tab> toRemove = getTabs().stream().filter(t -> {
            if (t instanceof EditorTab) {
                EditorTab editor = (EditorTab)t;
                return editor.getFile() == null && !editor.isUnsaved();
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        Path file = tab.getFile();
        if (file != null) {
            tabs.put(file, tab);
        }
        tab.setOnClosed(event -> {
            tabs.remove(file);
        });
        tab.setEditorContextMenu(menu);
        getTabs().add(getTabs().size() - 1, tab);
        getSelectionModel().select(tab);
        getTabs().removeAll(toRemove);
    }

    private EditorTab getOpenEditor() {
        return (EditorTab)getSelectionModel().getSelectedItem();
    }

    public boolean allowClosing() {
        boolean hasUnsaved = tabs.values().stream().map(tab -> (EditorTab)tab).anyMatch(EditorTab::isUnsaved);
        if (hasUnsaved) {
            ConfirmDialog alert = new ConfirmDialog("Exit without saving?");
            return alert.showAndWait().orElse(null) == ButtonType.OK;
        } else {
            return true;
        }
    }

    public void openFile(Path file) {
        Path path = file.toAbsolutePath().normalize();
        if (tabs.containsKey(path)) {
            getSelectionModel().select(tabs.get(path));
        } else {
            addTab(new EditorTab(path));
        }
    }

    public void saveOpenAs() {
        getOpenEditor().saveFileAs();
    }

    public void saveOpen() {
        getOpenEditor().saveFile();
    }

    public void saveOpenIfNamed() {
        EditorTab tab = getOpenEditor();
        if (tab.getFile() != null) {
            tab.saveFile();
        }
    }

    public Path getOpenFile() {
        return getOpenEditor().getFile();
    }

    public String getOpenContent() {
        return getOpenEditor().getEditorContent();
    }

    public Map<Path, Set<Integer>> getBreakpoints() {
        Map<Path, Set<Integer>> ret = new HashMap<>();
        for (Entry<Path, EditorTab> tab : tabs.entrySet()) {
            ret.put(tab.getKey(), tab.getValue().getBreakpoints());
        }
        return ret;
    }

    public void resetHighlighting() {
        for (EditorTab tab : tabs.values()) {
            tab.resetHighlighting();
        }
    }

    public void highlightDebug(Position position) {
        Path file = position.getFile();
        if (file != null) {
            openFile(file);
        }
        getOpenEditor().highlightDebug(position);
    }

    public void highlightError(DaroException error) {
        Path file = error.getFile();
        if (file != null) {
            openFile(file);
        }
        getOpenEditor().highlightError(error);
    }
}
