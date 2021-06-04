package daro.ide.editor;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import daro.lang.interpreter.DaroException;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class EditorTabs extends TabPane {
    private Map<Path, EditorTab> tabs;

    public EditorTabs() {
        tabs = new HashMap<>();
        setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
        sceneProperty().addListener((observable, old, scene) -> {
            KeyCombination keys = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
            scene.getAccelerators().put(keys, () -> {
                ((EditorTab)getSelectionModel().getSelectedItem()).saveFile();
            });
        });
        Tab addTab = new Tab("+");
        addTab.setClosable(false);
        addTab.getStyleClass().add("add-tab");
        addTab.setOnSelectionChanged(event -> {
            addTab(new EditorTab(""));
        });
        getTabs().add(addTab);
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
        getTabs().add(getTabs().size() - 1, tab);
        getSelectionModel().select(tab);
        getTabs().removeAll(toRemove);
    }

    public void openFile(Path file) {
        Path path = file.toAbsolutePath().normalize();
        if (tabs.containsKey(path)) {
            getSelectionModel().select(tabs.get(path));
        } else {
            addTab(new EditorTab(path));
        }
    }

    public void saveOpenIfNamed() {
        EditorTab tab = (EditorTab)getSelectionModel().getSelectedItem();
        if (tab.getFile() != null) {
            tab.saveFile();
        }
    }

    public Path getOpenFile() {
        return ((EditorTab)getSelectionModel().getSelectedItem()).getFile();
    }

    public String getOpenContent() {
        return ((EditorTab)getSelectionModel().getSelectedItem()).getEditorContent();
    }

    public void highlightError(DaroException error) {
        Path file = error.getFile();
        if (file != null) {
            openFile(file);
        }
        EditorTab editor = (EditorTab)getSelectionModel().getSelectedItem();
        editor.highlightError(error);
    }
}
