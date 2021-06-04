package daro.ide.debug;

import daro.lang.interpreter.Scope;
import javafx.scene.control.*;

public class ScopeTree extends TreeView<String> {

    public ScopeTree(Scope root) {
        super(new ScopeTreeItem(root));
        setShowRoot(false);
    }

    public void reload() {
        ((ScopeTreeItem)getRoot()).reload();
    }
}
