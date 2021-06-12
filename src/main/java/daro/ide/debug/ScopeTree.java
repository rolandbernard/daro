package daro.ide.debug;

import daro.lang.interpreter.Scope;
import javafx.scene.control.*;

/**
 * This class extends the {@link TreeView}. This {@link TreeView} can be used to
 * display the values inside of a {@link Scope}.
 * 
 * @author Roland Bernard
 */
public class ScopeTree extends TreeView<String> {

    /**
     * Create a new {@link ScopeTree} displaying the given scopes variables.
     *
     * @param root The root scope
     */
    public ScopeTree(Scope root) {
        super(new ScopeTreeItem(root));
        setShowRoot(false);
        ((ScopeTreeItem)getRoot()).expandBlockScopes();
    }

    /**
     * Reload the data of the scope.
     */
    public void reload() {
        ((ScopeTreeItem)getRoot()).reload();
        ((ScopeTreeItem)getRoot()).expandBlockScopes();
    }
}
