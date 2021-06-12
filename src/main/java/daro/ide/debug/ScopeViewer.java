package daro.ide.debug;

import java.util.Stack;

import daro.lang.interpreter.Scope;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

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
        setStack(new Stack<>());
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
     * Set the stack that should be displayed.
     *
     * @param stack The stack to display
     */
    public void setStack(Stack<StackContext> stack) {
        if (stack.size() > 0) {
            ListView<StackContext> list = new ListView<>();
            for (int i = stack.size() - 1; i >= 0; i--) {
                StackContext item = stack.get(i);
                list.getItems().add(item);
            }
            list.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                setScope(newValue.getScope());
            });
            Label label = new Label("Stack");
            VBox top = new VBox(label, list);
            setRight(top);
        } else {
            setRight(null);
        }
    }

    /**
     * Reload the data of the scope.
     */
    public void reload() {
        ((ScopeTree)getCenter()).reload();
    }

}
