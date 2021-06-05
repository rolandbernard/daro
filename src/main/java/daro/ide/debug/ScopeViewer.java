package daro.ide.debug;

import daro.lang.interpreter.Scope;
import javafx.scene.layout.BorderPane;

public class ScopeViewer extends BorderPane {
    private Scope currentScope;

    public ScopeViewer(Scope scope) {
        setScope(scope);
    }

    public void setScope(Scope scope) {
        if (!scope.equals(currentScope)) {
            currentScope = scope;
            ScopeTree view = new ScopeTree(scope);
            setCenter(view);
        }
    }

    public void reload() {
        ((ScopeTree)getCenter()).reload();
    }

}
