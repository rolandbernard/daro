package daro.ide.files;

import java.nio.file.Path;
import java.util.function.Consumer;

import javafx.scene.control.*;
import javafx.scene.input.MouseButton;

/**
 * This class can be used to display a file tree. The tree also includes file
 * operations like rename or creating new files and folders.
 * 
 * @author Roland Bernard
 */
public class FileTree extends TreeView<String> {
    private Consumer<Path> onFileOpen;
    private FileContextMenu menu;

    /**
     * Create a new file tree with the given root.
     *
     * @param root The root of the new tree
     */
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

    /**
     * Reload the file tree. This will show changes made since the last reload.
     */
    public void reload() {
        ((FileTreeItem)getRoot()).reload(true);
    }

    /**
     * Set the context of the trees context menu to the given item.
     *
     * @param item The item that the context menu should refer to
     */
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

    /**
     * Set the function that should be called for opening new files.
     *
     * @param onFileOpen The {@link Consumer} to call on file open
     */
    public void setOnFileOpen(Consumer<Path> onFileOpen) {
        this.onFileOpen = onFileOpen;
    }
}
