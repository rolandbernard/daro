package daro.ide.debug;

import daro.lang.values.DaroObject;
import javafx.scene.control.TreeItem;

/**
 * This class extends the {@link TreeItem}. This {@link TreeItem} can be used to
 * display the name and value of a variable.
 * 
 * @author Roland Bernard
 */
public class VariableTreeItem extends TreeItem<String> {
    private final String name;
    private final DaroObject value;

    /**
     * Create a new {@link VariableTreeItem} using the given name and value.
     *
     * @param name  The name of the item
     * @param value The value of the item
     */
    public VariableTreeItem(String name, DaroObject value) {
        super(name + " = " + value);
        this.name = name;
        this.value = value;
        super.getChildren().add(new ScopeTreeItem(value.getMemberScope()));
    }

    /**
     * Reload the data of the scope.
     */
    public void reload() {
        super.getChildren().forEach(item -> ((ScopeTreeItem)item).reload());
    }

    /**
     * Return the name of the displayed variable.
     *
     * @return The variable name
     */
    public String getName() {
        return name;
    }

    /**
     * Return the value of the displayed variable.
     *
     * @return The variable value
     */
    public DaroObject getVariable() {
        return value;
    }
}
