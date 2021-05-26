package daro.ide.files;

import java.nio.file.Path;
import java.util.function.Consumer;

import javafx.scene.control.*;
import javafx.scene.input.MouseButton;

public class FileTree extends TreeView<String> {
    private Consumer<Path> onFileOpen = null;
    private FileContextMenu menu = new FileContextMenu(); 

    public FileTree(Path file) {
        super(new FileTreeItem(file));
        setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) { 
                FileTreeItem selected = (FileTreeItem)getSelectionModel().getSelectedItem(); 
                if (selected != null) { 
                    openContextMenu(selected, event.getScreenX(), event.getScreenY()); 
                } 
            } else {
                menu.hide(); 
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) { 
                    if (onFileOpen != null) {
                        onFileOpen.accept(file);
                    }
                }
            }
        });
    }

    private void openContextMenu(FileTreeItem item, double x, double y) { 
        menu.setContext(item.getFile()); 
        menu.show(this, x, y); 
    } 

    public void setOnFileOpen(Consumer<Path> onFileOpen) {
        this.onFileOpen = onFileOpen;
    }
}


