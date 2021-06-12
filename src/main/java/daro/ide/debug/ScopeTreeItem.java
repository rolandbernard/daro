package daro.ide.debug;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import daro.lang.interpreter.BlockScope;
import daro.lang.interpreter.Scope;
import daro.lang.values.DaroObject;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 * This class extends the {@link TreeItem}. This {@link TreeItem} can be used to
 * display the values inside of a {@link Scope}.
 * 
 * @author Roland Bernard
 */
public class ScopeTreeItem extends TreeItem<String> {
    private final Scope scope;
    private boolean loaded;

    /**
     * Create a new {@link ScopeTreeItem} for the given scope.
     *
     * @param scope The scope the item represents
     */
    public ScopeTreeItem(Scope scope) {
        super(scope.getClass().getSimpleName());
        this.scope = scope;
    }

    /**
     * Expand all scopes starting from this one that are instances of {@link BlockScope}.
     */
    public void expandBlockScopes() {
        if (scope instanceof BlockScope) {
            ((BlockScope)scope).safeRecursion(() -> {
                setExpanded(true);
                for (TreeItem<String> item : getChildren()) {
                    ((ScopeTreeItem)item).expandBlockScopes();
                }
                return null;
            }, null);
        }
    }

    /**
     * Return the scope of this {@link ScopeTreeItem}.
     *
     * @return The scope
     */
    public Scope getScope() {
        return scope;
    }

    /**
     * Reload the data of the scope.
     */
    public void reload() {
        loaded = false;
        if (isExpanded()) {
            getChildren();
            for (TreeItem<String> item : super.getChildren()) {
                ((ScopeTreeItem)item).reload();
            }
        }
    }

    @Override
    public ObservableList<TreeItem<String>> getChildren() {
        if (!loaded) {
            loaded = true;
            Set<?> children = super.getChildren().stream().map(item -> {
                if (item instanceof VariableTreeItem) {
                    VariableTreeItem var = (VariableTreeItem)item;
                    return new SimpleEntry<>(var.getName(), var.getVariable());
                } else {
                    return ((ScopeTreeItem)item).getScope();
                }
            }).collect(Collectors.toSet());
            Set<Scope> parents = new HashSet<>(Arrays.asList(scope.getParents()));
            Set<Entry<String, DaroObject>> variables = scope.getFinalLevel().getCompleteMapping().entrySet();
            super.getChildren().removeIf(item -> {
                if (item instanceof VariableTreeItem) {
                    VariableTreeItem var = (VariableTreeItem)item;
                    return !variables.contains(new SimpleEntry<>(var.getName(), var.getVariable()));
                } else {
                    return !parents.contains(((ScopeTreeItem)item).getScope());
                }
            });
            super.getChildren().addAll(
                parents.stream()
                    .filter(parent -> !children.contains(parent))
                    .map(parent -> new ScopeTreeItem(parent))
                    .collect(Collectors.toList())
            );
            super.getChildren().addAll(
                variables.stream()
                    .filter(variable -> !children.contains(variable))
                    .map(variable -> new VariableTreeItem(variable.getKey(), variable.getValue()))
                    .collect(Collectors.toList())
            );
            super.getChildren().sort((itemA, itemB) -> {
                if (itemA instanceof VariableTreeItem && itemB instanceof VariableTreeItem) {
                    return itemA.getValue().compareTo(itemB.getValue());
                } else if (itemB instanceof VariableTreeItem) {
                    return 1;
                } else if (itemA instanceof VariableTreeItem) {
                    return -1;
                } else {
                    return itemA.getValue().compareTo(itemB.getValue());
                }
            });
        }
        return super.getChildren();
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}
