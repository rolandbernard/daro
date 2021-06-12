package daro.ide.debug;

import org.junit.jupiter.api.Test;

import daro.lang.interpreter.ConstantScope;
import daro.lang.interpreter.RootScope;
import daro.lang.interpreter.Scope;
import daro.lang.values.DaroArray;
import daro.lang.values.DaroReal;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

public class ScopeTreeTest {

    @Test
    void scopeTreeItemIsNotALeaf() {
        ScopeTreeItem item = new ScopeTreeItem(new RootScope());
        assertFalse(item.isLeaf());
    }

    @Test
    void scopeTreeItemStringValue() {
        ScopeTreeItem item = new ScopeTreeItem(new RootScope());
        assertEquals("RootScope", item.getValue());
    }

    @Test
    void scopeTreeItemGetScope() {
        Scope scope = new RootScope();
        ScopeTreeItem item = new ScopeTreeItem(scope);
        assertEquals(scope, item.getScope());
    }

    @Test
    void variableTreeItemIsALeaf() {
        VariableTreeItem item = new VariableTreeItem("a", new DaroReal(42));
        assertTrue(item.isLeaf());
    }

    @Test
    void variableTreeItemIsNotALeaf() {
        VariableTreeItem item = new VariableTreeItem("a", new DaroArray(List.of()));
        assertFalse(item.isLeaf());
    }

    @Test
    void variableTreeItemStringValue() {
        VariableTreeItem item = new VariableTreeItem("a", new DaroReal(42));
        assertEquals("a = 42.0", item.getValue());
    }

    @Test
    void variableTreeItemGetName() {
        VariableTreeItem item = new VariableTreeItem("a", new DaroReal(42));
        assertEquals("a", item.getName());
    }

    @Test
    void variableTreeItemGetVariable() {
        VariableTreeItem item = new VariableTreeItem("a", new DaroReal(42));
        assertEquals(new DaroReal(42), item.getVariable());
    }

    @Test
    void scopeTreeItemHasAllVariablesAsChildren() {
        Scope scope = new ConstantScope(Map.of("x", new DaroReal(1), "y", new DaroReal(2)));
        ScopeTreeItem item = new ScopeTreeItem(scope);
        assertEquals(2, item.getChildren().size());
    }

    @Test
    void scopeTreeItemHasParentsAsChildren() {
        Scope scope = new ConstantScope(Map.of("x", new DaroReal(1)), new RootScope());
        ScopeTreeItem item = new ScopeTreeItem(scope);
        assertEquals(2, item.getChildren().size());
    }

    @Test
    void scopeTreeItemHasMultipleParentsAsChildren() {
        Scope scope = new ConstantScope(Map.of("x", new DaroReal(1)), new RootScope(), new RootScope());
        ScopeTreeItem item = new ScopeTreeItem(scope);
        assertEquals(3, item.getChildren().size());
    }

    @Test
    void scopeTreeItemCanHandleRecursiveScopes() {
        ConstantScope scope = new ConstantScope(Map.of("x", new DaroReal(1)), new RootScope());
        scope.addParent(scope);
        ScopeTreeItem item = new ScopeTreeItem(scope);
        assertEquals(3, item.getChildren().size());
    }

    @Test
    void scopeTreeItemCanBeReloaded() {
        ConstantScope scope = new ConstantScope(Map.of("x", new DaroReal(1)), new RootScope());
        ScopeTreeItem item = new ScopeTreeItem(scope);
        item.setExpanded(true);
        assertEquals(2, item.getChildren().size());
        scope.addParent(scope);
        item.reload();
        assertEquals(3, item.getChildren().size());
    }
}
