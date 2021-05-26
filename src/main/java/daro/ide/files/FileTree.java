package daro.ide.files;

import java.nio.file.Path;
import java.util.function.Consumer;

import javafx.scene.control.*;
import javafx.scene.input.MouseButton;

public class FileTree extends TreeView<String> {
    private Consumer<Path> onFileOpen = null;
    private FileContextMenu menu = new FileContextMenu(); 

    public FileTree(Path root) {
        super(new FileTreeItem(root));
        setShowRoot(false);
        setOnMouseClicked(event -> {
            menu.hide(); 
            FileTreeItem selected = (FileTreeItem)getSelectionModel().getSelectedItem(); 
            if (event.getButton() == MouseButton.SECONDARY) { 
                openContextMenu(selected, event.getScreenX(), event.getScreenY()); 
            } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) { 
                if (selected != null && onFileOpen != null && selected.isLeaf()) {
                    onFileOpen.accept(selected.getFile());
                }
            }
        });
    }

    private void openContextMenu(FileTreeItem item, double x, double y) { 
        if (item == null) {
            menu.setContext(null, ((FileTreeItem)getRoot()).getFile(), (FileTreeItem)getRoot(), onFileOpen);
        } else {
            if (!item.isLeaf() && item.isExpanded()) {
                menu.setContext(item.getFile(), item.getFile(), (FileTreeItem)item, onFileOpen);
            } else {
                menu.setContext(item.getFile(), item.getFile().getParent(), (FileTreeItem)item.getParent(), onFileOpen);
            }
        }
        menu.show(this, x, y);
    } 

    public void setOnFileOpen(Consumer<Path> onFileOpen) {
        this.onFileOpen = onFileOpen;
    }
}


