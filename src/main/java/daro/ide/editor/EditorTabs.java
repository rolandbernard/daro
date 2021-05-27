package daro.ide.editor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class EditorTabs extends TabPane {
    private Map<Path, Tab> paths;
    private Map<Tab, Path> tabs;

    public EditorTabs() {
        paths = new HashMap<>();
        tabs = new HashMap<>();
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
                Tab tab = new Tab(file.getFileName().toString());
                String content;
                content = Files.readString(file);
                TextEditor editor = new TextEditor(content);
                tab.setContent(editor);
                paths.put(file, tab);
                tabs.put(tab, file);
                tab.setOnClosed(event -> {
                    paths.remove(file);
                    tabs.remove(tab);
                });
                getTabs().add(tab);
                getSelectionModel().select(tab);
            } catch (IOException e) { }
        }
    }
}
