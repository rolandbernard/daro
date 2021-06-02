package daro.ide.editor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class EditorTabs extends TabPane {
    private Map<Path, Tab> paths;
    private Map<Tab, Path> tabs;

    public EditorTabs() {
        paths = new HashMap<>();
        tabs = new HashMap<>();
        setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
        sceneProperty().addListener((observable, old, scene) -> {
            KeyCombination keys = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
            scene.getAccelerators().put(keys, () -> {
                Tab tab = getSelectionModel().getSelectedItem();
                TextEditor editor = (TextEditor)tab.getContent();
                Path file = getOpenFile();
                String content = getOpenContent();
                try {
                    Files.writeString(file, content);
                    editor.setUnsaved(false);
                    tab.setText(file.getFileName().toString());
                } catch (IOException e) { }
            });
        });
    }

    public boolean hasUnsavedFile() {
        return tabs.keySet().stream()
            .map(tab -> (TextEditor)tab.getContent())
            .anyMatch(TextEditor::hasUnsaved);
    }

    public Path getOpenFile() {
        return tabs.get(getSelectionModel().getSelectedItem());
    }

    public String getOpenContent() {
        return ((TextEditor)getSelectionModel().getSelectedItem().getContent()).getText();
    }

    public void openFile(Path file) {
        if (paths.containsKey(file)) {
            getSelectionModel().select(paths.get(file));
        } else {
            try {
                String filename = file.getFileName().toString();
                Tab tab = new Tab(filename);
                String content = Files.readString(file);
                TextEditor editor = new TextEditor(content);
                editor.setOnChange(value -> {
                    tab.setText(filename + "+");
                });
                tab.setContent(editor);
                paths.put(file, tab);
                tabs.put(tab, file);
                tab.setOnCloseRequest(event -> {
                    if (!editor.hasUnsaved()) {
                        paths.remove(file);
                        tabs.remove(tab);
                    } else {
                        ConfirmDialog alert = new ConfirmDialog("Close without saving?");
                        if (alert.showAndWait().orElse(null) == ButtonType.OK) {
                            paths.remove(file);
                            tabs.remove(tab);
                        } else {
                            event.consume();
                        }
                    }
                });
                getTabs().add(tab);
                getSelectionModel().select(tab);
            } catch (IOException e) { }
        }
    }
}
