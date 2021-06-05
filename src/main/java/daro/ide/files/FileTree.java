package daro.ide.files;

import java.nio.file.Path;
import java.util.function.Consumer;

import javafx.scene.control.*;
import javafx.scene.input.MouseButton;

public class FileTree extends TreeView<String> {
    private Consumer<Path> onFileOpen;
    private FileContextMenu menu;

    public FileTree(Path root) {
        super(new FileTreeItem(root));
        onFileOpen = null;
        menu = new FileContextMenu();
        setShowRoot(false);
        setOnMouseClicked(event -> {
            menu.hide();
            FileTreeItem selected = (FileTreeItem)getSelectionModel().getSelectedItem();
            setMenuContext(selected);
            if (event.getButton() == MouseButton.SECONDARY) {
                menu.show(this, event.getScreenX(), event.getScreenY());
            } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                if (selected != null && onFileOpen != null && selected.isLeaf()) {
                    onFileOpen.accept(selected.getFile());
                }
            }
        });
    }

    public void reload() {
        ((FileTreeItem)getRoot()).reload(true);
    }

    private void setMenuContext(FileTreeItem item) {
        if (item == null) {
            menu.setContext(null, ((FileTreeItem)getRoot()).getFile(), (FileTreeItem)getRoot(), onFileOpen);
        } else {
            if (!item.isLeaf() && item.isExpanded()) {
                menu.setContext(item.getFile(), item.getFile(), (FileTreeItem)item, onFileOpen);
            } else {
                menu.setContext(item.getFile(), item.getFile().getParent(), (FileTreeItem)item.getParent(), onFileOpen);
            }
        }
    }

    public void setOnFileOpen(Consumer<Path> onFileOpen) {
        this.onFileOpen = onFileOpen;
    }
}
