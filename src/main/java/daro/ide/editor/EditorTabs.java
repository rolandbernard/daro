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

/**
 * This class implements a {@link TabPane} containing multiple {@link EditorTab}
 * tabs.
 * 
 * @author Roland Bernard
 */
public class EditorTabs extends TabPane {
    private Map<Path, EditorTab> tabs;
    private EditorContextMenu menu;

    /**
     * Create a new {@link EditorTabs} object.
     */
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
            if (newValue instanceof EditorTab) {
                ((EditorTab)newValue).setEditorContextMenu(menu);
            }
        });
        setContextMenu(menu);
    }

    /**
     * Add a new tab to the list of tabs.
     *
     * @param tab The tab that should be added
     */
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

    /**
     * Get the {@link EditorTab} that is currently visible to the user.
     *
     * @return The displayed {@link EditorTab}
     */
    private EditorTab getOpenEditor() {
        return (EditorTab)getSelectionModel().getSelectedItem();
    }

    /**
     * Execute the following before closing the {@link EditorTabs}. This will ast
     * the user if he wants to throw away unsaved changes, if there are any.
     *
     * @return true if the user allows closing, false otherwise
     */
    public boolean allowClosing() {
        boolean hasUnsaved = tabs.values().stream().map(tab -> (EditorTab)tab).anyMatch(EditorTab::isUnsaved);
        if (hasUnsaved) {
            ConfirmDialog alert = new ConfirmDialog("Exit without saving?");
            return alert.showAndWait().orElse(null) == ButtonType.OK;
        } else {
            return true;
        }
    }

    /**
     * Open the given file in the editor. If the file is already open simply select
     * the already existing tab, otherwise create a new one.
     *
     * @param file The file to open
     */
    public void openFile(Path file) {
        Path path = file.toAbsolutePath().normalize();
        if (tabs.containsKey(path)) {
            getSelectionModel().select(tabs.get(path));
        } else {
            addTab(new EditorTab(path));
        }
    }

    /**
     * Save the currently open editor, asking the user which file should be saved
     * to.
     */
    public void saveOpenAs() {
        getOpenEditor().saveFileAs();
    }

    /**
     * Save the currently open editor.
     */
    public void saveOpen() {
        getOpenEditor().saveFile();
    }

    /**
     * Save the currently open editor, only if it is linked to a file.
     */
    public void saveOpenIfNamed() {
        EditorTab tab = getOpenEditor();
        if (tab.getFile() != null) {
            tab.saveFile();
        }
    }

    /**
     * Return the {@link Path} of the file that is currently open.
     *
     * @return The currently open file
     */
    public Path getOpenFile() {
        return getOpenEditor().getFile();
    }

    /**
     * Return the content of the tab that is currently open.
     *
     * @return The currently open text
     */
    public String getOpenContent() {
        return getOpenEditor().getEditorContent();
    }

    /**
     * Get the complete map of currently set breakpoints.
     *
     * @return The currently set breakpoints
     */
    public Map<Path, Set<Integer>> getBreakpoints() {
        Map<Path, Set<Integer>> ret = new HashMap<>();
        for (Entry<Path, EditorTab> tab : tabs.entrySet()) {
            ret.put(tab.getKey(), tab.getValue().getBreakpoints());
        }
        return ret;
    }

    /**
     * Reset the highlighting of the tabs editor. This will also clear the currently
     * displayed debug position and error.
     */
    public void resetHighlighting() {
        for (Tab tab : getTabs()) {
            if (tab instanceof EditorTab) {
                ((EditorTab)tab).resetHighlighting();
            }
        }
    }

    /**
     * Highlight the given position as the debuggers current position. This will
     * automatically open the file associated with the position.
     *
     * @param position The position to mark
     */
    public void highlightDebug(Position position) {
        Path file = position.getFile();
        if (file != null) {
            openFile(file);
        }
        getOpenEditor().highlightDebug(position);
    }

    /**
     * Highlight the given position as the location of an error. This will
     * automatically open the file associated with the errors position.
     *
     * @param error The error to mark
     */
    public void highlightError(DaroException error) {
        Path file = error.getFile();
        if (file != null) {
            openFile(file);
        }
        getOpenEditor().highlightError(error);
    }
}
