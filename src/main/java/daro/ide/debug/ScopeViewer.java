package daro.ide.debug;

import daro.lang.interpreter.Scope;
import javafx.scene.layout.BorderPane;

/**
 * This class can be used to display a scope in for of a tree.
 * 
 * @author Roland Bernard
 */
public class ScopeViewer extends BorderPane {
    private Scope currentScope;

    /**
     * Create a new {@link ScopeViewer} displaying the given scope.
     *
     * @param scope The scope to display
     */
    public ScopeViewer(Scope scope) {
        setScope(scope);
    }

    /**
     * Set the scope that should be displayed.
     *
     * @param scope The scope to display
     */
    public void setScope(Scope scope) {
        if (!scope.equals(currentScope)) {
            currentScope = scope;
            ScopeTree view = new ScopeTree(scope);
            setCenter(view);
        }
    }

    /**
     * Reload the data of the scope.
     */
    public void reload() {
        ((ScopeTree)getCenter()).reload();
    }

}
